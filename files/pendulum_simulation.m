% pendulum_simulation.m
% This script simulates the motion of a simple pendulum

% Step 1: Define the pendulum parameters
g = 9.81;  % Gravitational acceleration (m/s^2)
L = 1;     % Length of the pendulum (m)
theta0 = pi/4;  % Initial angle (radians)
omega0 = 0;     % Initial angular velocity (rad/s)

% Step 2: Define the equations of motion (second-order ODE)
% d2theta/dt^2 = -(g/L)*sin(theta)
pendulum_eq = @(t, y) [y(2); -(g/L) * sin(y(1))];

% Step 3: Solve the ODE using ode45
[t, y] = ode45(pendulum_eq, [0, 10], [theta0, omega0]);

% Step 4: Plot the results (angle vs time)
figure;
plot(t, y(:,1), 'LineWidth', 2);
title('Pendulum Motion');
xlabel('Time (s)');
ylabel('Angle (radians)');
grid on;

% Step 5: Display the maximum angle reached
max_angle = max(y(:,1));
disp(['Maximum Angle Reached: ', num2str(max_angle), ' radians']);
