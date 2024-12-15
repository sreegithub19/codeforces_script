% Define a multi-line shell command
shell_command = [
    'echo "Hello from shell!"; ' ...
    'echo "Current directory:"; ' ...
    'pwd; ' ...
    'ls ' ...
    'python3 -c "print(222222)"'
];

% Execute the shell command
[status, cmdout] = system(shell_command);

% Display output and status
disp('Shell command output:');
disp(cmdout);
disp(['Status: ', num2str(status)]);
