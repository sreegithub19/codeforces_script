import 'package:dart_eval/dart_eval.dart';

void main() {
  print(eval('2 + 2')); // -> 4
  
  final program = r'''
      class Cat {
        Cat(this.name);
        final String name;
        String speak() => "I'm $name!";
      }
      void main() {
        final cat = Cat('Fluffy');
        print(cat.speak()); // Print inside the program
      }
  ''';
  
  eval(program, function: 'main'); // Calls main(), which prints 'I'm Fluffy!'
}
