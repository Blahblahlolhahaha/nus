NETWORKS_DIR = networks

TASKS_DIR = tasks

SRC_MININET_DIR = mininet
SRC_P4_DIR = p4
SRC_CONTROLLER_DIR = controller

BUILD_DIR = build
BUILD_P4_DIR = $(BUILD_DIR)/p4

TEMP_DIR = temp
TEMP_P4_SWITCH_DIR = $(TEMP_DIR)/p4_switch
TEMP_P4RT_CONTROLLER_DIR = $(TEMP_DIR)/p4rt_controller

TESTS_DIR = tests
TESTS_CHECKS_DIR = checks

P4_COMPILER = p4c-bm2-ss
P4_COMPILER_OUT_NAME = main
P4_COMPILER_ARGS += --p4v 16
P4_COMPILER_ARGS += --p4runtime-files $(BUILD_P4_DIR)/$(P4_COMPILER_OUT_NAME).p4info.txtpb
P4_COMPILER_ARGS += -o $(BUILD_P4_DIR)/$(P4_COMPILER_OUT_NAME).json

NETWORK_DEFINITION_FILE = $(SRC_MININET_DIR)/networks.py

CLI_NETWORK_FILE = $(SRC_MININET_DIR)/topology_cli.py

P4_FILE = $(SRC_P4_DIR)/main.p4

CONTROLLER_SCRIPT_FILE = $(SRC_CONTROLLER_DIR)/controller.py
CONTROLLER_LOG_FILE = $(TEMP_P4RT_CONTROLLER_DIR)/controller-stdout.log
CONTROLLER_PID_FILE = $(TEMP_P4RT_CONTROLLER_DIR)/controller.pid

CHECKS_NETWORK_FILE = $(SRC_MININET_DIR)/topology_checks.py
CHECKS_CASES_FILE = $(TESTS_CHECKS_DIR)/traffic_checker.py

PYTHON_INTERPRETER = /opt/p4/p4dev-python-venv/bin/python3

.PHONY: \
	build-init \
	build-task-1 \
	build-task-2 \
	build-task-3 \
	build-task-4 \
	build-task-5 \
	build-clean \
	run-init \
	run-task-1 \
	run-checks-1 \
	run-task-2 \
	run-checks-2 \
	run-task-3 \
	run-checks-3 \
	run-task-4 \
	run-checks-4 \
	run-task-5 \
	run-checks-5 \
	run-stop \
	run-clean \
	clean \
	generate-submission

all:

build-init:
	mkdir -p $(BUILD_P4_DIR)

build-task-1:
	$(MAKE) build-clean
	$(MAKE) build-init
	$(P4_COMPILER) $(P4_COMPILER_ARGS) $(TASKS_DIR)/1/$(P4_FILE)

build-task-2:
	$(MAKE) build-clean
	$(MAKE) build-init
	$(P4_COMPILER) $(P4_COMPILER_ARGS) $(TASKS_DIR)/2/$(P4_FILE)

build-task-3:
	$(MAKE) build-clean
	$(MAKE) build-init
	$(P4_COMPILER) $(P4_COMPILER_ARGS) $(TASKS_DIR)/3/$(P4_FILE)

build-task-4:
	$(MAKE) build-clean
	$(MAKE) build-init
	$(P4_COMPILER) $(P4_COMPILER_ARGS) $(TASKS_DIR)/4/$(P4_FILE)

build-task-5:
	$(MAKE) build-clean
	$(MAKE) build-init
	$(P4_COMPILER) $(P4_COMPILER_ARGS) $(TASKS_DIR)/5/$(P4_FILE)

build-clean:
	rm -rf $(BUILD_DIR)

run-init:
	mkdir -p $(TEMP_P4RT_CONTROLLER_DIR)
	mkdir -p $(TEMP_P4_SWITCH_DIR)

run-task-1:
	$(MAKE) run-clean
	$(MAKE) build-task-1
	$(MAKE) run-init
	$(PYTHON_INTERPRETER) $(TASKS_DIR)/1/$(CONTROLLER_SCRIPT_FILE) > $(CONTROLLER_LOG_FILE) 2>&1 & echo $$! > $(CONTROLLER_PID_FILE)
	sudo $(PYTHON_INTERPRETER) $(NETWORKS_DIR)/1/$(CLI_NETWORK_FILE)
	$(MAKE) run-stop

run-checks-1:
ifeq ("$(wildcard $(TESTS_DIR)/1/$(TESTS_CHECKS_DIR))", "")
	$(error The requested checks are unavailable)
endif
	$(MAKE) run-clean
	$(MAKE) build-task-1
	$(MAKE) run-init
	$(PYTHON_INTERPRETER) $(TASKS_DIR)/1/$(CONTROLLER_SCRIPT_FILE) > $(CONTROLLER_LOG_FILE) 2>&1 & echo $$! > $(CONTROLLER_PID_FILE)
	sudo $(PYTHON_INTERPRETER) $(NETWORKS_DIR)/1/$(CHECKS_NETWORK_FILE)
	$(MAKE) run-stop
	$(PYTHON_INTERPRETER) $(TESTS_DIR)/1/$(CHECKS_CASES_FILE)

run-task-2:
	$(MAKE) run-clean
	$(MAKE) build-task-2
	$(MAKE) run-init
	$(PYTHON_INTERPRETER) $(TASKS_DIR)/2/$(CONTROLLER_SCRIPT_FILE) > $(CONTROLLER_LOG_FILE) 2>&1 & echo $$! > $(CONTROLLER_PID_FILE)
	sudo $(PYTHON_INTERPRETER) $(NETWORKS_DIR)/2/$(CLI_NETWORK_FILE)
	$(MAKE) run-stop

run-checks-2:
ifeq ("$(wildcard $(TESTS_DIR)/2/$(TESTS_CHECKS_DIR))", "")
	$(error The requested checks are unavailable)
endif
	$(MAKE) run-clean
	$(MAKE) build-task-2
	$(MAKE) run-init
	$(PYTHON_INTERPRETER) $(TASKS_DIR)/2/$(CONTROLLER_SCRIPT_FILE) > $(CONTROLLER_LOG_FILE) 2>&1 & echo $$! > $(CONTROLLER_PID_FILE)
	sudo $(PYTHON_INTERPRETER) $(NETWORKS_DIR)/2/$(CHECKS_NETWORK_FILE)
	$(MAKE) run-stop
	$(PYTHON_INTERPRETER) $(TESTS_DIR)/2/$(CHECKS_CASES_FILE)

run-task-3:
	$(MAKE) run-clean
	$(MAKE) build-task-3
	$(MAKE) run-init
	$(PYTHON_INTERPRETER) $(TASKS_DIR)/3/$(CONTROLLER_SCRIPT_FILE) > $(CONTROLLER_LOG_FILE) 2>&1 & echo $$! > $(CONTROLLER_PID_FILE)
	sudo $(PYTHON_INTERPRETER) $(NETWORKS_DIR)/3/$(CLI_NETWORK_FILE)
	$(MAKE) run-stop

run-checks-3:
ifeq ("$(wildcard $(TESTS_DIR)/3/$(TESTS_CHECKS_DIR))", "")
	$(error The requested checks are unavailable)
endif
	$(MAKE) run-clean
	$(MAKE) build-task-3
	$(MAKE) run-init
	$(PYTHON_INTERPRETER) $(TASKS_DIR)/3/$(CONTROLLER_SCRIPT_FILE) > $(CONTROLLER_LOG_FILE) 2>&1 & echo $$! > $(CONTROLLER_PID_FILE)
	sudo $(PYTHON_INTERPRETER) $(NETWORKS_DIR)/3/$(CHECKS_NETWORK_FILE)
	$(MAKE) run-stop
	$(PYTHON_INTERPRETER) $(TESTS_DIR)/3/$(CHECKS_CASES_FILE)

run-task-4:
	$(MAKE) run-clean
	$(MAKE) build-task-4
	$(MAKE) run-init
	$(PYTHON_INTERPRETER) $(TASKS_DIR)/4/$(CONTROLLER_SCRIPT_FILE) > $(CONTROLLER_LOG_FILE) 2>&1 & echo $$! > $(CONTROLLER_PID_FILE)
	sudo $(PYTHON_INTERPRETER) $(NETWORKS_DIR)/4/$(CLI_NETWORK_FILE)
	$(MAKE) run-stop

run-checks-4:
ifeq ("$(wildcard $(TESTS_DIR)/4/$(TESTS_CHECKS_DIR))", "")
	$(error The requested checks are unavailable)
endif
	$(MAKE) run-clean
	$(MAKE) build-task-4
	$(MAKE) run-init
	$(PYTHON_INTERPRETER) $(TASKS_DIR)/4/$(CONTROLLER_SCRIPT_FILE) > $(CONTROLLER_LOG_FILE) 2>&1 & echo $$! > $(CONTROLLER_PID_FILE)
	sudo $(PYTHON_INTERPRETER) $(NETWORKS_DIR)/4/$(CHECKS_NETWORK_FILE)
	$(MAKE) run-stop
	$(PYTHON_INTERPRETER) $(TESTS_DIR)/4/$(CHECKS_CASES_FILE)

run-task-5:
	$(MAKE) run-clean
	$(MAKE) build-task-5
	$(MAKE) run-init
	$(PYTHON_INTERPRETER) $(TASKS_DIR)/5/$(CONTROLLER_SCRIPT_FILE) > $(CONTROLLER_LOG_FILE) 2>&1 & echo $$! > $(CONTROLLER_PID_FILE)
	sudo $(PYTHON_INTERPRETER) $(NETWORKS_DIR)/5/$(CLI_NETWORK_FILE)
	$(MAKE) run-stop

run-checks-5:
ifeq ("$(wildcard $(TESTS_DIR)/5/$(TESTS_CHECKS_DIR))", "")
	$(error The requested checks are unavailable)
endif
	$(MAKE) run-clean
	$(MAKE) build-task-5
	$(MAKE) run-init
	$(PYTHON_INTERPRETER) $(TASKS_DIR)/5/$(CONTROLLER_SCRIPT_FILE) > $(CONTROLLER_LOG_FILE) 2>&1 & echo $$! > $(CONTROLLER_PID_FILE)
	sudo $(PYTHON_INTERPRETER) $(NETWORKS_DIR)/5/$(CHECKS_NETWORK_FILE)
	$(MAKE) run-stop
	$(PYTHON_INTERPRETER) $(TESTS_DIR)/5/$(CHECKS_CASES_FILE)

run-stop:
	sudo mn -c
	for pid_file in $$(find temp -type f -name "*.pid"); do sudo kill -9 $$(cat $$pid_file | sed "s/^[[:space:]]*//;s/[[:space:]]*$$//") || true; sudo rm -rf $$pid_file; done

run-clean:
	$(MAKE) run-stop
	sudo rm -rf $(TEMP_DIR)

clean:
	$(MAKE) run-clean
	$(MAKE) build-clean

generate-submission:
	@read -p "Student ID: " student_id; \
	echo "*** Generating submission file"; \
	zip -r $$student_id.zip tasks;
