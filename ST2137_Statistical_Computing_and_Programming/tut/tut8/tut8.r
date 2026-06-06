setwd("C:\\Users\\USER\\Documents\\GitHub\\nus\\ST2137_Statistical_Computing_and_Programming\\tut\\tut8")

machine <- read.csv("machine.txt", sep=" ")

machine

old <- machine[machine$machine == "O", "strength"]
new <- machine[machine$machine == "N", "strength"]
var.test(old,new)
t.test(new, old, paired=TRUE, alternative="greater", var.equal=TRUE)

flextime = read.csv("flextime.txt", sep=" ")
before <- flextime$before
after <- flextime$after

wilcox.test(after, before, paired=TRUE, alternative="greater") # non-parametric oops

stack(flextime[,c("before", "after")])


weeklies = read.csv("weeklies.txt")
