rebuild: clean backend
	mvn package

backend: clean
	mvn -pl backend install

clean:
	mvn clean
	rm -rf ~/.m2/repository/br/usp/ime/arranger/
