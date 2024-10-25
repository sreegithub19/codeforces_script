import 'dart:io';

void main() async {
  // Dart code to run
  String dartCode = "void main() { print('Hello, inline Dart execution!'); }";

  // Prepare the command to run the Dart code
  var process = await Process.start('dart', ['--enable-asserts', '-']);

  // Write the Dart code to the process's stdin
  process.stdin.writeln(dartCode);
  await process.stdin.close();

  // Capture the output
  var output = await process.stdout.transform(SystemEncoding().decoder).join();
  var errors = await process.stderr.transform(SystemEncoding().decoder).join();

  // Print the output
  print(output);

  // Print any errors
  if (errors.isNotEmpty) {
    print('Errors:');
    print(errors);
  }

  // Wait for the process to exit
  await process.exitCode;
}
