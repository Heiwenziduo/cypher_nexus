package com.github.heiwenziduo.cypher_nexus.mechanic

class CypherNotFoundException(override val message: String?) : Exception(message) {
}