package PS_TEMPLATE_BASE_PACKAGE_NAME

import ninja.leaping.configurate.objectmapping.Setting
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable

@ConfigSerializable
class Config(@Setting val version: Int = 1)
