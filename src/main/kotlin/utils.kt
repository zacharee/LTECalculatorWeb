import com.ionspin.kotlin.bignum.integer.BigInteger

val bands = arrayOf(
    "1/2100/IMT",
    "2/1900/PCS",
    "3/1800/DCS",
    "4/1700/AWS-1",
    "5/850/Cellular",
    "7/2600/IMT-E",
    "8/900/Ext. GSM",
    "11/1500/Lower PDC",
    "12/700/Lower SMH",
    "13/700/Upper SMH",
    "14/700/Upper SMH",
    "17/700/Lower SMH",
    "18/850/Lower 800 (Japan)",
    "19/850/Upper 800 (Japan)",
    "20/800/Digital Dividend (EU)",
    "21/1500/Upper PDC",
    "24/1600/Upper L-Band (US)",
    "25/1900/Ext. PCS",
    "26/850/Ext. Cellular",
    "28/700/APT",
    "29/700/Lower SMH",
    "30/2300/WCS",
    "31/450/NMT",
    "32/1500/L-Band (EU)",
    "34/2000/IMT",
    "37/1900/PCS",
    "38/2600/IMT-E",
    "39/1900/DCS-IMT Gap",
    "40/2300/S-Band",
    "41/2500/BRS",
    "42/3500/CBRS (EU, Japan)",
    "43/3700/C-Band",
    "44/700/APT",
    "46/5200/U-NII",
    "47/5900/U-NII-4",
    "48/3500/CBRS (US)",
    "49/3500/C-Band",
    "50/1500/L-Band (EU)",
    "51/1500/L-Band Ext. (EU)",
    "52/3300/C-Band",
    "53/2400/S-Band",
    "65/2100/Ext. IMT",
    "66/1700/Ext. AWS (1–3)",
    "67/700/EU 700",
    "68/700/ME 700",
    "69/2600/IMT-E",
    "70/1700/Suppl. AWS (2–4)",
    "71/600/Digital Dividend (US)",
    "72/450/PMR (EU)",
    "73/450/PMR (APT)",
    "74/1500/Lower L-Band (US)",
    "75/1500/L-Band (EU)",
    "76/1500/L-Band Ext. (EU)",
    "85/700/Ext. Lower SMH",
    "87/410/PMR (APT)",
    "88/410/PMR (EU)"
)

/**
 * Do the magic of getting an NV value from a band.
 * Qualcomm's LTE NV format is pretty simple. In binary,
 * each LTE band is a digit, starting from the least-
 * significant (rightmost).
 *
 * For example, bands 1,2,3,5 would be 10111 in binary, or 23
 * in decimal.
 *
 * ==================================
 *
 * One possible way to calculate the value is to take the bands
 * the user has selected (say 1,2,3,5) and loop through them.
 * Each band is represented by a 1, and its digits place is its number.
 * Take the current band sum (which starts at 0) and add to it.
 * Shift that addition left by (band_number - 1).
 *
 * For example, adding band 1 will add a
 * 1 shifted left 0 places. Adding band 2 will add a 1 shifted left 1
 * place. Adding band 3 will add a 1 shifted left 2 places. And so on.
 *
 * The output in binary will be 10111, which is 23 in decimal.
 *
 * ==================================
 *
 * However, by the time we reach somewhere between bands 53 and 65,
 * we hit a Long overflow. Even using a BigInteger implementation, it
 * fails for some reason. So instead, we can just use 2^(band - 1).
 * This does the same thing as the bit shifting, but in base-10,
 * and seems to work more reliably.
 */
@ExperimentalUnsignedTypes
fun calculateBandNumber(selectedBands: Collection<Int>): BigInteger {
    var bands = BigInteger(0)

    selectedBands.forEach {
        if (it < 1) throw IllegalArgumentException("Band number must be > 1")
//        bands = bands or (BigInteger(1).shl(it - 1))
        bands = bands + BigInteger(2).pow(BigInteger(it - 1))
    }

    return bands
}

@ExperimentalUnsignedTypes
fun calculateListFromBandNumber(bandNumber: BigInteger): List<Int> {
    val bands = ArrayList<Int>()

    bandNumber.toString(2).reversed().forEachIndexed { index, c ->
        if (c == '1') bands.add(index + 1)
    }

    return bands
}

data class BandInfo(
    val band: Int,
    val freq: Int,
    val desc: String
) {
    companion object {
        fun getFromString(input: String): BandInfo {
            val split = input.split("/")

            return BandInfo(split[0].toInt(), split[1].toInt(), split[2])
        }
    }
}