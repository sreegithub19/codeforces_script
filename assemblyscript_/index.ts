// arithmetic.ts
export function add(a: i32, b: i32): i32 {
  return a + b;
}

export function subtract(a: i32, b: i32): i32 {
  return a - b;
}

export function multiply(a: i32, b: i32): i32 {
  return a * b;
}

export function divide(a: i32, b: i32): f32 {
  if (b == 0) return 0;  // Avoid division by zero
  return <f32>a / <f32>b;
}
