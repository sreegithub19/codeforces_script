import 'dart:io';

void main() async {
  // Dart code to run
  String dartCode = "void main() { print('Hello, inline Dart execution ins!'); }";

  // Prepare the command
  var result = await Process.run('bash', ['-c', 'echo "$dartCode" | dart']);

  // Print the output
  print(result.stdout);
}
