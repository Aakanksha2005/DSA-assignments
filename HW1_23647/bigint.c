#include <stdio.h>
#include <stdlib.h>
#include "bigint.h"

// Create a BigInt with a given size and sign
BigInt* bigint_create(int size, int sign) {
  BigInt* num = (BigInt*)malloc(sizeof(BigInt));
  num->size = size;
  num->sign = sign;
  num->digits = (int*)calloc(size, sizeof(int));  // Initialize to zero
  return num;
}

// Destroy a BigInt, freeing allocated memory
void bigint_destroy(BigInt* num) {
  free(num->digits);
  free(num);
}

// Set a BigInt from an integer value
void bigint_set_int(BigInt* num, int value) {
  // Handle negative values
  num->sign = (value < 0) ? -1 : 1;
  value = abs(value);

  // Convert integer to digit array
  int temp = value;
  int size = 0;
  while (temp > 0) {
    size++;
    temp /= 10;
  }

  num->size = size;
  num->digits = (int*)realloc(num->digits, size * sizeof(int)); // Resize digits array

  for (int i = 0; i < size; i++) {
    num->digits[i] = value % 10;
    value /= 10;
  }
}

// Compare two BigInts
int bigint_compare(const BigInt* a, const BigInt* b) {
  // Compare signs first
  if (a->sign != b->sign) {
    return a->sign - b->sign;
  }

  // Compare magnitudes
  if (a->size != b->size) {
    return a->sign * (a->size - b->size);
  }

  for (int i = a->size - 1; i >= 0; i--) {
    if (a->digits[i] != b->digits[i]) {
      return a->sign * (a->digits[i] - b->digits[i]);
    }
  }

  return 0; // Equal
}

// Subtract two BigInts
BigInt* bigint_subtract(const BigInt* a, const BigInt* b) {
  // Handle cases where one number is zero
  if (bigint_compare(a, b) == 0) {
    BigInt* result = bigint_create(1, 0); // Return zero
    result->digits[0] = 0;
    return result;
  }

  // Determine larger and smaller numbers
  int sign = 1;
  const BigInt* larger = a;
  const BigInt* smaller = b;
  if (bigint_compare(a, b) < 0) {
    sign = -1;
    larger = b;
    smaller = a;
  }

  // Allocate memory for the result
  BigInt* result = bigint_create(larger->size, sign);

  int borrow = 0;
  for (int i = 0; i < larger->size; i++) {
    int diff = larger->digits[i] - borrow;
    if (i < smaller->size) {
      diff -= smaller->digits[i];
    }
    if (diff < 0) {
      diff += 10;
      borrow = 1;
    } else {
      borrow = 0;
    }
    result->digits[i] = diff;
  }

  // Remove leading zeros
  while (result->size > 1 && result->digits[result->size - 1] == 0) {
    result->size--;
  }

  return result;
}
