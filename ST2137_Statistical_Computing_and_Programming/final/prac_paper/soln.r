setwd("c:\\Users\\sad\\Documents\\GitHub\\nus\\ST2137_Statistical_Computing_And_Programming\\lect")

concrete <- read.csv("data/concrete+slump+test/slump_test.data")

names(concrete)[names(concrete) == "No"] <- "id"
names(concrete)[names(concrete) == "Compressive.Strength..28.day..Mpa."] <- "Comp_Strength"
names(concrete)[names(concrete) == "FLOW.cm."] <- "Flow"

comp_str_lm = lm(Comp_Strength ~ Cement + Slag + Fly.ash + Water + SP + Coarse.Aggr., data = concrete)
summary(comp_str_lm)
comp_str_lm2 = lm(Comp_Strength ~ Cement + Slag + Fly.ash + Water + SP + Coarse.Aggr. + Fine.Aggr., data = concrete)
summary(comp_str_lm2)
r1 <- rstandard(comp_str_lm)
plot(y=r1, x=concrete$Fine.Aggr.)
abline(h=0, lty=2, col="red")
sad <- influence.measures(comp_str_lm)
summary(sad)
ids = c(8,14,49)
test <- ecdf(concrete$Water)
test(concrete$Water)[ids]


library(DescTools)
x <- matrix(c(1, 3, 10, 6,
2, 3, 10, 7,
1, 6, 14, 12,
0, 1, 9, 11), ncol=4, byrow=TRUE)
dimnames(x) <- list(c("<15,000", "15,000-25,000", "25,000-40,000",
">40,000"),
c("Very Dissat.", "Little Dissat.", "Mod. Sat.",
"Very Sat."))
us_svy_tab <- as.table(x)
output <- Desc(x, plotit = FALSE, verbose = 3)
output[[1]][["assocs"]][3,1]
