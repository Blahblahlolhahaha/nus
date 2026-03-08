x <- matrix (c(3, 1, 1, 3), nrow = 2)

milk_tab <- as.table(x)
milk_tab

dhyper(3,4,4,4)

vec1 <- c('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j')
result <- vec1[2:8][-2]
length(result)
result

data <- read.csv("pyp/student-mat.csv", sep=";")
cols = data[, c("address", "guardian")]

rural_mother <- cols[cols$address == "R" & cols$guardian == "mother",]

proportion = NROW(rural_mother) / NROW(cols)
print(proportion)

address_guardian <-  table(cols)

sad <- chisq.test(address_guardian)$expected
sad

60.82025 / sum(sad)

data <- read.csv("pyp/student-mat.csv", sep=";")
library("DescTools")

rural <- table(data[data$address == "R", c("sex","romantic")])


urban <- table(data[data$address == "U", c("sex","romantic")])

rural_or = OddsRatio(rural,conf.level = .95)
urban_or = OddsRatio(urban,conf.level = .95)

output <- list(
    rural = list(or=rural_or[1], ci = rural_or[-1]),
    urban = list(or=urban_or[1], ci = urban_or[-1]))
output
