package com.example.SecretService

/**
 * Created by menta on 29/03/2018.
 */
import java.lang.Math.pow

fun List<Int>.innerProduct(B:List<Int>): Int {
    return zip(B)
            .map { it.first * it.second }
            .sum()
}

fun List<Int>.vandermond(): List<List<Int>> {
    return map { xval -> List<Int>(size, {it:Int -> pow(xval.toDouble(), it.toDouble()).toInt()}) }
}


fun List<Int>.polyeval(input: Int): Int {
    return foldIndexed(0, {index:Int, soFar:Int, next:Int -> soFar + next*pow(input.toDouble(), index.toDouble()).toInt() % 2147483647})

    //foldRight(0, { soFar, alpha -> alpha + soFar * input })
}

fun List<Int>.polyAdd(summand:List<Int>): List<Int> {
    return mapIndexed{index, next -> next + summand.get(index)}
}

fun List<Int>.polyMult(multipland: List<Int>): List<Int> {
    return foldIndexed( multipland , { index, soFar, next -> soFar
            .polyAdd(List<Int>( index,{0}) + multipland.map{it*next} )
    })
}

fun List<List<Int>>.transpose(): List<List<Int>> {
    return mapIndexed{ rownum, row -> row.mapIndexed{ colnum, col -> get(colnum)[rownum] }
    }
}

fun List<List<Int>>.submatrix( droprow: Int, dropcol: Int ): List<List<Int>> {
    return filterIndexed { index, _ -> index != droprow }
            .map { it.filterIndexed { index, _ -> index != dropcol } }
            .fold( listOf(), { current, next -> current.plusElement(next) } )
}

fun List<List<Int>>.det(): Int {
    return if (size > 1) mapIndexed { colnum, _ -> submatrix(0, colnum).det() }
            .mapIndexed { index, entry -> entry * pow(-1.0, index.toDouble()).toInt() }
            .innerProduct( get(0) ) else get(0)[0]
}

fun List<List<Int>>.cofactor(): List<List<Int>> {
    return mapIndexed { rownum: Int, row: List<Int> -> row.mapIndexed { colnum, _ -> submatrix(rownum, colnum).det()* pow(-1.0, (colnum+rownum).toDouble()).toInt()  }
    }
}

fun List<List<Int>>.invert(): List<List<Int>>{
    return cofactor()
            .transpose()
            .map{ row -> row
                    .map{ entry -> entry }} //divide det()
}

fun List<List<Int>>.matrixMultiplyRight( B: List<List<Int>> ): List<List<Int>> {
    return mapIndexed { rownum:Int, brow -> brow
            .mapIndexed { colnum:Int, _ -> brow
                    .innerProduct( B.transpose()[colnum] )
            }
    }
}