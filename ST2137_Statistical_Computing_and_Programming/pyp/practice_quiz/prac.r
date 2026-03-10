data <- read.csv("pyp/practice_quiz/wip.txt", sep = " ")
library(lattice)
bwplot(time~plant, data, horizontal = FALSE)

sad <- cat("sad")

liverpool <- read.csv("pyp/practice_quiz/liverpool_2223_season.csv")
range_gf <- range(liverpool$GF)
gf_vals <- range_gf[1]:range_gf[2]
print(gf_vals)
gf_tbl <- rep(0, length = length(gf_vals))
names(gf_tbl) <- gf_vals
gf_tbl
tmp_table <- table(liverpool$GF)
gf_tbl[names(tmp_table)] <- tmp_table

barplot(gf_tbl)

func_y <- function(x) {
    return(sin(x) - x/2 - 0.1)
}

uniroot(func_y, c(0,1))

student <- read.csv("pyp/practice_quiz/student-mat.csv", sep=";")

address_guardian <- table(student[,c("address", "guardian")])

address_guardian[3] / sum(address_guardian)

chisq.test(address_guardian)$expected[1, 2] / sum(chisq.test(address_guardian)$expected)

rural_tab = table(student[student$address == "R", c("sex", "romantic")])
rural_tab

urban_tab <- table(student[student$address == "U", c("sex", "romantic")])
rural_tab

library(DescTools)
rural_or <- OddsRatio(rural_tab, conf.level = .95)
urban_or <- OddsRatio(urban_tab, conf.level = .95)

list(
    rural = list(or=rural_or[1], ci = c(rural_or[2], rural_or[3])),
    urban = list(or = urban_or[1], ci = c(urban_or[2], urban_or[3]))
)
