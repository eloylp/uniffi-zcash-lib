import uniffi.zcash.*

val supp = TestSupport.fromCsvFile()

fun testOrchardAddressFromRawAddressBytes() {
	val rawBytes = supp.getAsU8Array("orchard_address")
	val zoa = ZcashOrchardAddress.fromRawAddressBytes(rawBytes)

	assert(zoa.toRawAddressBytes() == rawBytes)
}
testOrchardAddressFromRawAddressBytes()

fun testOrchardAddressDiversifier() {
	val rawBytes = supp.getAsU8Array("orchard_address")
	val expectedBytes = supp.getAsU8Array("orchard_diversifier")
	val zoa = ZcashOrchardAddress.fromRawAddressBytes(rawBytes)
	val diver = zoa.diversifier().toBytes()

	assert(diver == expectedBytes)
}
testOrchardAddressDiversifier()

fun testOrchardIvkToPaymentAddress() {
	val seed = supp.getAsU8Array("seed")

    val unifiedSpendingKey = ZcashUnifiedSpendingKey.fromSeed(
        ZcashConsensusParameters.MAIN_NETWORK,
        seed,
        ZcashAccountId(0u),
    )

    val orchardDiversifier = ZcashOrchardDiversifier.fromBytes(listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0).map { it.toUByte() })

    val expectedBytes = supp.getAsU8Array("orchard_address")

    val rawAddressBytes = unifiedSpendingKey.toUnifiedFullViewingKey()
        .orchard()!!.toIvk(ZcashOrchardScope.EXTERNAL)
        .address(orchardDiversifier)!!.toRawAddressBytes()

    assert(rawAddressBytes == expectedBytes)
}
testOrchardIvkToPaymentAddress()
