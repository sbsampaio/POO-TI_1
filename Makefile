all: clean compile run

compile:
	javac -d bin src/*.java

run:
	java -cp bin App

clean:
	rm -f *.class