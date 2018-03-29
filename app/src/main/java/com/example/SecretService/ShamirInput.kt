package com.example.SecretService

/**
 * Created by menta on 29/03/2018.
 */

class MasterKey(val coefficients: List<Int>) {
    fun message(): Int {
        return coefficients[0]
    }
}

class ShamirKey(val secretName: String, val keyName: String, val index: Int, val point: Int)

class Secret(val secretName: String, val masterkey: MasterKey) {

    fun meta(): String {
        return secretName//and MAC later
    }

    fun keygen(keyName: String, index: Int): ShamirKey {
        return ShamirKey(
                secretName,
                keyName,
                index,
                masterkey.coefficients.polyeval(index)
        )
    }
}

fun shamirEncrypt(secretName: String, message: Int, threshold: Int): Secret {
    return Secret(
            secretName,
            MasterKey(
                    List<Int>(threshold, { index -> if (index == 0) message else (Math.random() * 4294967291).toInt() })
            )
    )
}

fun shamirDecrypt(input: List<ShamirKey>): Secret {
    return Secret(
            input.first().secretName,
            MasterKey(
                    input.mapIndexed { keynum, _ -> input
                            .map(ShamirKey::index)
                            .vandermond()
                            .invert()[keynum]
                            .innerProduct(input
                                    .map(ShamirKey::point))
                    }
            )
    )
}