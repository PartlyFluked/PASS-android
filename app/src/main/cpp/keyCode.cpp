//
// Created by menta on 23/03/2018.
//
#include "keyCode.h"

using namespace std;

//Sets bitsize of keys used
#define BITSIZE 256
#define BIGPRIME "0xFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F"

//Recursive function for calculating f(x)
mpz_class keyFunc(int i, int j, vector<mpz_class> &coefficients);
mpz_class recKeyFunc(int i, int j, vector<mpz_class> &coefficients);

//Generates keys to distribute based on the secret and solve on threshold.
void generateKeyset(const char * secret, int threshold, int keys, char ** keySet) {
    vector<mpz_class> randList; //List of random coefficients
    gmp_randclass r1(gmp_randinit_default);
    mpz_class prime256;
    mpz_set_str(prime256.get_mpz_t(), BIGPRIME, 0);
    for (int i = 0; i < threshold; i++) {
        mpz_class rand = r1.get_z_bits(BITSIZE);
        if (rand > prime256) { i--; continue; }
        randList.push_back(rand); //Generates number between 0 and 2^BITSIZE
    }
    mpz_class bigSecret;
    mpz_set_str(bigSecret.get_mpz_t(), secret, 58);
    //mpz_class bigSecret(secret, 58);
    randList.push_back(bigSecret); //Appends secret to the back of the random coeffs.
    memcpy(&keySet[0], &secret, (strlen(secret)+1)*sizeof(char *));
    mpz_class num;
    for (int i = 1; i <= keys; i++) {
        num = recKeyFunc(i, threshold, randList) % prime256;
        string numStr = num.get_str(58);
        char * newNum = (char *) malloc((numStr.size()+1)*sizeof(char *));
        memcpy(newNum, numStr.c_str(), numStr.size()+1);
        memcpy(&keySet[i], &newNum, numStr.size()+1);
    }
    return;
}

mpz_class keyFunc(int i, int j, vector <mpz_class> &coefficients) {
    mpz_class ans, x, base = 10, exp = 1;
    for (int l = j - 1; l >= 0; l--) {
        x = i;
        mpz_pow_ui(x.get_mpz_t(), base.get_mpz_t(), exp.get_ui());
        ans += coefficients[l] * x;
    }
    return ans;
}

mpz_class recKeyFunc(int x, int j, vector <mpz_class> &coefficients) {
    if (j==0) { return coefficients[j]; }
    return coefficients[j] + x*recKeyFunc(x, --j, coefficients); //f(x) = S + ax + bx^2 + ...
}


//Uses gaussian elimination to solve the simulatanaeous eqns described by the keySet.
vector<mpz_class> decodeKeyset(vector<mpz_class> &keySet) {
    vector< vector<mpz_class> > matrix;
    for (int i = 0; i < keySet.size(); i++) {
        matrix.push_back(vector<mpz_class>());
        matrix[i].push_back(1);
        for (int j = 0; j < keySet.size(); j++) {
            matrix[i].push_back(pow((i+1), (j+1)));
        }
        for (int j = 0; j <= keySet.size(); j++) {
            if (j==i) { matrix[i].push_back(1); }
            else { matrix[i].push_back(0); }
        }
        //matrix[i].push_back(keySet[i]);
    }
    int n = matrix.size();

    for (int i = 0; i<n; i++) {
        // Search for maximum in this column
        mpz_class maxEl = matrix[i][i];
        int maxRow = i;
        for (int k = i + 1; k<n; k++) {
            if (matrix[k][i] > maxEl) {
                maxEl = matrix[k][i];
                maxRow = k;
            }
        }

        // Swap maximum row with current row (column by column)
        for (int k = i; k<n + 1; k++) {
            mpz_class tmp = matrix[maxRow][k];
            matrix[maxRow][k] = matrix[i][k];
            matrix[i][k] = tmp;
        }

        // Make all rows below this one 0 in current column
        for (int k = i + 1; k<n; k++) {
            mpz_class c = -matrix[k][i] / matrix[i][i];
            for (int j = i; j<n + 1; j++) {
                if (i == j) {
                    matrix[k][j] = 0;
                }
                else {
                    matrix[k][j] += c * matrix[i][j];
                }
            }
        }
    }

    // Solve equation Ax=b for an upper triangular matrix A
    vector<mpz_class> x(n);
    for (int i = n - 1; i >= 0; i--) {
        x[i] = matrix[i][n] / matrix[i][i];
        for (int k = i - 1; k >= 0; k--) {
            matrix[k][n] -= matrix[k][i] * x[i];
        }
    }
    return x;
}