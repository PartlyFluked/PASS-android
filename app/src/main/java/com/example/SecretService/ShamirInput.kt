package com.example.SecretService

import java.util.Random

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

    fun keygen(keyName: String, input: Int): ShamirKey {
        return ShamirKey(
                secretName,
                keyName,
                input,
                masterkey.coefficients.polyeval(input)
        )
    }
}

fun shamirEncrypt(secretName: String, message: Int, threshold: Int, rndGen: Random): Secret {
    return Secret(
            secretName,
            MasterKey(
                    List<Int>(
                            threshold,
                            { index ->
                                if (index == 0)
                                message
                            else (rndGen.nextInt(2147483647))
                            }
                    )
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