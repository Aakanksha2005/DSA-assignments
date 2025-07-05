#ifndef BIGINT_H
#define BIGINT_H

typedef struct BigInt {
  int size; // Number of digits (absolute value)
  int sign; // 1 for positive, -1 for negative
  int *digits; // Array to store digits (least significant digit first)
} BigInt;

// Function prototypes
BigInt* bigint_create(int size, int sign);
void bigint_destroy(BigInt* num);
void bigint_set_int(BigInt* num, int value);
int bigint_compare(const BigInt* a, const BigInt* b);
BigInt* bigint_subtract(const BigInt* a, const BigInt* b);

#endif
