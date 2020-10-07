# calculator
 A simple calculator to perform operations like add, sub, mult, div and let.

Assumptions made: When you perform div operations, only the integer part will be taken for result. 

i.e. -> div(9,2) will return 4
 
 Checkout the repository and navigate to the location of the repository where it was downloaded and perform the following at the project root directory using terminal/cmd:
 1. Run: mvn install

    to install required dependencies.

 2. Run: mvn compile

    to compile and make sure the project builds fine

 3. Run: mvn test

    to run the unit tests in the project.

 4. Run: mvn clean package

    to create a JAR for the application. The JAR gets created inside the /target directory
 5. Navigate to target directory and run the JAR using: 

    java -jar calculator-1.0-SNAPSHOT.jar "add(1, 1)"

 6. To run with a specific level of logging verbosity use the following command:

    java -jar -Dlog4j.debug -Dlogging.verbosity=DEBUG calculator-1.0-SNAPSHOT.jar "add(1, mult(2, 3))"

    java -jar -Dlog4j.info -Dlogging.verbosity=INFO calculator-1.0-SNAPSHOT.jar "mult(add(2, 2), div(9, 3))"

Travis CI link: https://travis-ci.org/github/vishal266/calculator
