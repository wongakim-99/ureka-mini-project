JAVAC    = javac
JAVA     = java
SRC_DIR  = src
BIN_DIR  = bin
LIB_DIR  = lib
CP       = $(LIB_DIR)/mysql-connector-j-8.4.0.jar:$(LIB_DIR)/lombok.jar
MAIN     = cinema.Main

build:
	find $(SRC_DIR) -name "*.java" > .sources.txt
	$(JAVAC) -cp $(CP) -d $(BIN_DIR) @.sources.txt
	cp $(SRC_DIR)/cinema/util/sql.properties $(BIN_DIR)/cinema/util/
	cp $(SRC_DIR)/cinema/util/db.properties  $(BIN_DIR)/cinema/util/
	rm .sources.txt

run: build
	$(JAVA) -cp $(BIN_DIR):$(CP) $(MAIN)

clean:
	rm -rf $(BIN_DIR)/cinema

.PHONY: build run clean
