setwd("C:\\Users\\USER\\Documents\\GitHub\\nus\\ST2137_Statistical_Computing_and_Programming\\tut\\tut8")

machine <- read.csv("machine.txt", sep=" ")

machine

old <- machine[machine$machine == "O", "strength"]
new <- machine[machine$machine == "N", "strength"]

t.test(old, new, paired=TRUE)

flextime = read.csv("flextime.txt", sep=" ")
before <- flextime$before
after <- flextime$after

t.test(before, after, paired = TRUE)

stack(flextime[,c("before", "after")])
