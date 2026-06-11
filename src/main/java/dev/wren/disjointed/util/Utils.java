package dev.wren.disjointed.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.valkyrienskies.core.api.bodies.ServerVsBody;
import org.valkyrienskies.core.api.bodies.properties.BodyPose;
import org.valkyrienskies.core.internal.joints.VSJointMaxForceTorque;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class Utils {

    public static Vector3d fromVec3(Vec3 vec3) {
        return new Vector3d(vec3.x, vec3.y, vec3.z);
    }

    public static Vector3d getActualPosition(Entity entity) {
        Vector3d pos = fromVec3(entity.position());
        return new Vector3d(pos.x, pos.y + (entity.getBoundingBox().getYsize() / 2), pos.z);
    }

    public static float getMassforBody(ServerLevel level, Long id) {
        ServerVsBody body = VSGameUtilsKt.getShipObjectWorld(level).getAllBodies().getById(id);

        return body != null ? (float) body.getInertiaData().getMass() : 1000f;
    }

    public static VSJointMaxForceTorque getMaxForceTorque(ServerLevel level, Long id0, Long id1) {
        float mass0 = getMassforBody(level, id0);
        float mass1 = getMassforBody(level, id1);

        float maxForce = 5e13f * Math.min(Math.max(mass0, mass1) / Math.min(mass0, mass1), 20.0f);

        return new VSJointMaxForceTorque(maxForce, maxForce);
    }

    public static BodyPose createBodyPose(Vec3 position, AABB boundingBox) {
        return createBodyPose(new Vector3d(position.x, position.y + (boundingBox.getYsize() / 2), position.z));
    }

    public static BodyPose createBodyPose(Vector3d position) {
        return new BodyPose() {
            @Override
            public @NotNull Vector3dc getPosition() {
                return new Vector3d(position);
            }

            @Override
            public @NotNull Quaterniondc getRotation() {
                return new Quaterniond();
            }
        };
    }

    public static String listAsString(List<?> list) {
        StringBuilder builder = new StringBuilder("[");
        for (Object o : list) {
            builder.append(o.toString());
            builder.append(list.indexOf(o) == list.size() - 1 ? "]" : ", ");
        }
        return builder.toString();
    }

    public static float pxToBlocks(float pixels) {
        return pixels / 16f;
    }

    public static double pxToBlocks(double pixels) {
        return pixels / 16;
    }

    public static void getSkin(String username, Consumer<ResourceLocation> callback) {
        Thread skinThread = new Thread(() -> {
            try {
                URL uuidUrl = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
                HttpURLConnection uuidConn = (HttpURLConnection) uuidUrl.openConnection();
                uuidConn.setRequestMethod("GET");

                if (uuidConn.getResponseCode() != 200) {
                    return;
                }

                JsonObject uuidJson = JsonParser.parseString(
                        new String(uuidConn.getInputStream().readAllBytes())
                ).getAsJsonObject();

                String uuidRaw = uuidJson.get("id").getAsString();
                UUID uuid = UUID.fromString(
                        uuidRaw.replaceFirst(
                                "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                                "$1-$2-$3-$4-$5"
                        )
                );

                GameProfile profile = new GameProfile(uuid, username);
                GameProfile filled = Minecraft.getInstance()
                        .getMinecraftSessionService()
                        .fillProfileProperties(profile, true);

                if (filled.getProperties().get("textures").isEmpty()) {
                    return;
                }

                Minecraft.getInstance().execute(() ->
                        Minecraft.getInstance().getSkinManager().registerSkins(
                                filled,
                                (type, location, textures) -> {
                                    if (type == MinecraftProfileTexture.Type.SKIN)
                                        callback.accept(location);
                                },
                                true
                        )
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        skinThread.setDaemon(true);
        skinThread.start();
    } // i hate this

    public static <E extends Enum<E>, V> CompoundTag writeMap(Map<E, V> map, TriConsumer<CompoundTag, String, V> writeFunc) {
        CompoundTag tag = new CompoundTag();

        for (Map.Entry<E, V> value : map.entrySet()) {
            writeFunc.accept(tag, value.getKey().name(), value.getValue());
        }

        return tag;
    }

    public static <E extends Enum<E>, V> EnumMap<E, V> readMap(CompoundTag tag, List<E> keys, Class<E> enumClass, BiFunction<CompoundTag, String, V> readFunc) {
        EnumMap<E, V> map = new EnumMap<>(enumClass);

        for (E key : keys) {
            map.put(key, readFunc.apply(tag, key.name()));
        }

        return map;
    }
}
