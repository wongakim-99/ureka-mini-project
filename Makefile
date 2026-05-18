JAVAC   = javac
JAVA    = java
SRC_DIR = src/main/java
RES_DIR = src/main/resources
BIN_DIR = bin
LIB_DIR = lib
CP      = $(LIB_DIR)/mysql-connector-j-8.4.0.jar:$(LIB_DIR)/lombok.jar
MAIN    = common.ui.Main

build:
	find $(SRC_DIR) -name "*.java" > .sources.txt
	$(JAVAC) -cp $(CP) -d $(BIN_DIR) @.sources.txt
	cp $(RES_DIR)/sql.properties $(BIN_DIR)/
	@if [ -f $(RES_DIR)/db.properties ]; then cp $(RES_DIR)/db.properties $(BIN_DIR)/; fi
	rm .sources.txt

run: build
	$(JAVA) -cp $(BIN_DIR):$(CP) $(MAIN)

clean:
	rm -rf $(BIN_DIR)/*

.PHONY: build run clean
