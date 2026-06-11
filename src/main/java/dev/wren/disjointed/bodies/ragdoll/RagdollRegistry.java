package dev.wren.disjointed.bodies.ragdoll;

import dev.wren.disjointed.bodies.ragdoll.client.ClientRagdollRenderer;
import dev.wren.disjointed.bodies.ragdoll.client.renderer.EnderDragonRagdollRenderer;
import dev.wren.disjointed.bodies.ragdoll.client.renderer.PlayerRagdollRenderer;
import dev.wren.disjointed.bodies.ragdoll.client.renderer.SlimPlayerRagdollRenderer;
import dev.wren.disjointed.bodies.ragdoll.packet.AddRagdollPacket;
import dev.wren.disjointed.bodies.ragdoll.types.EnderDragonRagdoll;
import dev.wren.disjointed.bodies.ragdoll.types.PlayerRagdoll;
import dev.wren.disjointed.bodies.ragdoll.types.SlimPlayerRagdoll;
import dev.wren.disjointed.util.Utils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class RagdollRegistry {
    private static final Map<String, RagdollSpec<?>> REGISTRY = new HashMap<>();

    public static <E extends Enum<E>> void register(String type, Class<E> enumClass, ClientRagdollRenderer<E> renderer, Function<CompoundTag, Ragdoll<E>> deserializer, BiFunction<String, FriendlyByteBuf, AddRagdollPacket<E>> decoder, RagdollFactory<E> factory) {
        REGISTRY.put(type, new RagdollSpec<>(renderer, deserializer, decoder, factory, enumClass));
    }

    public static <E extends Enum<E>> void register(String type, Class<E> enumClass, ClientRagdollRenderer<E> renderer, Function<CompoundTag, Ragdoll<E>> deserializer, RagdollFactory<E> factory) {
        register(type, enumClass, renderer, deserializer, decoder(enumClass), factory);
    }

    public static <E extends Enum<E>> void register(String type, Class<E> enumClass, BiFunction<UUID, EnumMap<E, Long>, Ragdoll<E>> constructor, List<E> slots, ClientRagdollRenderer<E> renderer, RagdollFactory<E> factory) {
        register(type, enumClass, renderer, genericDeserialize(constructor, enumClass, slots), decoder(enumClass), factory);
    }

    public static <E extends Enum<E>> Ragdoll<E> deserialize(CompoundTag nbt) {
        String id = nbt.getString("type");
        RagdollSpec<E> spec = get(id);
        if (spec == null || spec.deserializer == null) throw new IllegalArgumentException("Unknown ragdoll type: " + id);

        return spec.deserializer.apply(nbt);
    }

    public static <E extends Enum<E>> AddRagdollPacket<E> decode(FriendlyByteBuf buf) {
        String typeId = buf.readUtf();
        RagdollSpec<E> spec = get(typeId);
        if (spec == null || spec.decoder == null) throw new IllegalArgumentException("Unknown ragdoll type: " + typeId);

        return spec.decoder.apply(typeId, buf);
    }

    public static <E extends Enum<E>> ClientRagdollRenderer<E> getRenderer(String typeId) {
        RagdollSpec<E> spec = get(typeId);
        return spec == null ? null : spec.renderer;
    }

    public static <E extends Enum<E>> RagdollFactory<E> getFactory(String typeId) {
        RagdollSpec<E> spec = get(typeId);
        return spec == null ? null : spec.factory;
    }


    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> RagdollSpec<E> get(String typeId) {
        return (RagdollSpec<E>) REGISTRY.get(typeId);
    }

    public static boolean isValidType(String typeId) {
        return REGISTRY.containsKey(typeId);
    }

    public static Set<String> getTypes() {
        return REGISTRY.keySet();
    }

    public static void init() {
        register(
                "player",
                RagdollSlots.Humanoid.class,
                new PlayerRagdollRenderer(),
                tag -> {
                    UUID uuid = tag.getUUID("uuid");
                    String username = tag.getString("username");

                    EnumMap<RagdollSlots.Humanoid, Long> ids = Utils.readMap(tag.getCompound("pieces"), RagdollSlots.Humanoid.asList(), RagdollSlots.Humanoid.class, CompoundTag::getLong);

                    return new PlayerRagdoll(uuid, username, ids);
                },
                ((level, pos, args, isStatic) -> PlayerRagdoll.create(level, pos, args.get(0), isStatic))
        );

        register(
                "player_slim",
                RagdollSlots.Humanoid.class,
                new SlimPlayerRagdollRenderer(),
                tag -> {
                    UUID uuid = tag.getUUID("uuid");
                    String username = tag.getString("username");

                    EnumMap<RagdollSlots.Humanoid, Long> ids = Utils.readMap(tag.getCompound("pieces"), RagdollSlots.Humanoid.asList(), RagdollSlots.Humanoid.class, CompoundTag::getLong);

                    return new SlimPlayerRagdoll(uuid, username, ids);
                },
                ((level, pos, args, isStatic) -> SlimPlayerRagdoll.create(level, pos, args.get(0), isStatic))
        );

        register(
                "ender_dragon",
                RagdollSlots.EnderDragon.class,
                EnderDragonRagdoll::new,
                RagdollSlots.EnderDragon.asList(),
                new EnderDragonRagdollRenderer(),
                ((level, pos, args, isStatic) -> EnderDragonRagdoll.create(level, pos, isStatic)));
    }

    public static <E extends Enum<E>, R extends Ragdoll<E>> Function<CompoundTag, R> genericDeserialize(BiFunction<UUID, EnumMap<E, Long>, R> constructor, Class<E> enumClass, List<E> slots) {
        return tag -> {
            UUID uuid = tag.getUUID("uuid");
            EnumMap<E, Long> ids = Utils.readMap(tag.getCompound("pieces"), slots, enumClass, CompoundTag::getLong);

            return constructor.apply(uuid, ids);
        };
    }

    public static <E extends Enum<E>> BiFunction<String, FriendlyByteBuf, AddRagdollPacket<E>> decoder(Class<E> enumClass) {
        return (type, buf) -> {
            UUID uuid = buf.readUUID();
            CompoundTag extraData = buf.readNbt();

            int size = buf.readInt();
            EnumMap<E, Long> pieces = new EnumMap<>(enumClass);
            for (int i = 0; i < size; i++) {
                pieces.put(buf.readEnum(enumClass), buf.readLong());
            }

            return new AddRagdollPacket<>(uuid, type, pieces, extraData);
        };
    }

    public record RagdollSpec<E extends Enum<E>>(ClientRagdollRenderer<E> renderer, Function<CompoundTag, Ragdoll<E>> deserializer, BiFunction<String, FriendlyByteBuf, AddRagdollPacket<E>> decoder, RagdollFactory<E> factory, Class<E> enumClass) {}

}
