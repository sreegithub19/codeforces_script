// Matrix class to represent 2x2 matrix
export class Matrix {
    a: i32;
    b: i32;
    c: i32;
    d: i32;
  
    constructor(a: i32, b: i32, c: i32, d: i32) {
      this.a = a;
      this.b = b;
      this.c = c;
      this.d = d;
    }
  
    // Matrix addition
    add(other: Matrix): Matrix {
      return new Matrix(
        this.a + other.a,
        this.b + other.b,
        this.c + other.c,
        this.d + other.d
      );
    }
  
    // Matrix multiplication
    multiply(other: Matrix): Matrix {
      return new Matrix(
        this.a * other.a + this.b * other.c,
        this.a * other.b + this.b * other.d,
        this.c * other.a + this.d * other.c,
        this.c * other.b + this.d * other.d
      );
    }
  
    // Print matrix values (just for debugging)
    print(): void {
      console.log(`Matrix: [${this.a}, ${this.b}, ${this.c}, ${this.d}]`);
    }
  }
  
  // Function to calculate the dot product of two vectors
  export function dotProduct(vec1: Int32Array, vec2: Int32Array): i32 {
    if (vec1.length !== vec2.length) {
      throw new Error("Vectors must have the same length.");
    }
  
    let result: i32 = 0;
    for (let i = 0; i < vec1.length; i++) {
      result += vec1[i] * vec2[i];
    }
    return result;
  }
  
  // Function to create an identity matrix
  export function identityMatrix(): Matrix {
    return new Matrix(1, 0, 0, 1);
  }
  
  // Function to create a matrix from an array (e.g., [a, b, c, d])
  export function fromArray(arr: Int32Array): Matrix {
    if (arr.length !== 4) {
      throw new Error("Array must contain exactly 4 elements.");
    }
    return new Matrix(arr[0], arr[1], arr[2], arr[3]);
  }
  
  // Function to multiply two arrays element-wise
  export function arrayMultiply(arr1: Int32Array, arr2: Int32Array): Int32Array {
    let result = new Int32Array(arr1.length);
    for (let i = 0; i < arr1.length; i++) {
      result[i] = arr1[i] * arr2[i];
    }
    return result;
  }
  
  // Function to sum all elements in an array
  export function arraySum(arr: Int32Array): i32 {
    let sum: i32 = 0;
    for (let i = 0; i < arr.length; i++) {
      sum += arr[i];
    }
    return sum;
  }
  