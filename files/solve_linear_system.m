% solve_linear_system.m
% This script solves a system of linear equations Ax = b

% Step 1: Define the matrix A and vector b
A = [3, 2, -1; 2, -1, 3; -1, 2, 4];
b = [1; 2; 3];

% Step 2: Solve the system of linear equations
x = A\b;

% Step 3: Display the solution
disp('The solution to the system of linear equations is:');
disp(x);

% Step 4: Verify the solution
verification = A * x;
disp('Verification (A*x should equal b):');
disp(verification);
