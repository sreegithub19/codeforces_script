import subprocess

def run_dart_code(dart_code):
    # Prepare the Dart command with the eval option
    command = f'dart --no-sound-null-safety -e "{dart_code}"'
    
    # Run the command
    result = subprocess.run(command, shell=True, capture_output=True, text=True)
    return result.stdout, result.stderr

dart_code = '''
import 'package:dart_eval/dart_eval.dart';

void main() {
    print(eval('2 + 2')); // -> 4
    
    final program = r\'\'\'
        class Cat {
        Cat(this.name);
        final String name;
        String speak() => "I'm $name!";
        }
        void main() {
        final cat = Cat('Fluffy');
        print(cat.speak()); // Print inside the program
        }
      \'\'\';
      
      eval(program, function: 'main'); // Calls main(), which prints 'I'm Fluffy!'
}
'''

output, error = run_dart_code(dart_code)
print("Output:\n", output)
if error:
    print("Error:\n", error)
