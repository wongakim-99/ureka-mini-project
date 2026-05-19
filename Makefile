GRADLE = ./gradlew

build:
	$(GRADLE) build

run:
	$(GRADLE) run

clean:
	$(GRADLE) clean

.PHONY: build run clean
