setwd("c:\\Users\\USER\\Documents\\GitHub\\nus\\ST2137_Statistical_Computing_And_Programming\\lect")

sad <- read.csv("data\\concrete+slump+test\\slump_test.data")
# print(sad)
# abline(a = 30, b = 1, col = "Red")

library(lattice)


library(psych)
col_to_use = c("Cement", "Slag", "Compressive Strength (28-day)(Mpa)", "Water", "SLUMP.cm.", "FLOW.cm.")
corPlot(cor(sad[, col_to_use]), cex = 0.8)
