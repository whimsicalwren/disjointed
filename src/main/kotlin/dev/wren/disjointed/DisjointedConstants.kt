package dev.wren.disjointed

import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.LogManager

const val MODID: String = "disjointed"
const val NAME: String = "Disjointed"

val LOGGER: Logger = LogManager.getLogger()

fun asResource(path: String) = ResourceLocation.fromNamespaceAndPath(MODID, path)