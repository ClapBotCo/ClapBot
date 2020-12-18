package joecord.seal.clapbot.api

abstract class ClapDirector(
    id: String
) {
    /**
     * Uniquely identifying string for this [ClapDirector], not meant to be readable. Use full
     * package name to ensure uniqueness
     */
    val id = id
}