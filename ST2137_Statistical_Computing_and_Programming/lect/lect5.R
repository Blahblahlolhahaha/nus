setwd("c:\\Users\\USER\\Documents\\GitHub\\nus\\ST2137_Statistical_Computing_And_Programming\\lect")

x <- matrix(c(46, 37, 474, 516), nrow = 2)
dimnames(x) <- list(c("male", "female"), c("pain", "no pain"))
chest_tab <- as.table(x)
print(chest_tab)
chisq_output <- chisq.test(chest_tab)
print(chisq_output)
print(chisq_output$expected)


y <- matrix(c(4, 2, 184, 260), nrow = 2)
dimnames(y) <- list(c("claritin", "placebo"), c("nervous", "not nervous"))
claritin_tab <- as.table(y)
print(claritin_tab)

fisher.test(claritin_tab)
chisq.test(claritin_tab)
sad = dhyper(0:6, 188, 262, 6) # w = 4 # 6 is sample size 188 = n 262 = m
sum(sort(sad)[1:4])

x <- matrix(c(762,327,468,484,239,477), ncol=3, byrow=TRUE)
dimnames(x) <- list(c("female", "male"),
c("Dem", "Ind", "Rep"))
political_tab <- as.table(x)
print(political_tab)
political_chi = chisq.test((political_tab))
political_chi$residuals


library(DescTools)
OddsRatio(chest_tab, conf.level = .95)

x <- matrix(c(1, 3, 10, 6,
2, 3, 10, 7,
1, 6, 14, 12,
0, 1, 9, 11), ncol=4, byrow=TRUE)
dimnames(x) <- list(c("<15,000", "15,000-25,000", "25,000-40,000", ">40,000"),
c("Very Dissat.", "Little Dissat.", "Mod. Sat.", "Very Sat"))
us_svy_tab <- as.table(x)
output <- Desc(x, plotit = TRUE, verbose = 3)
output[[1]]$assocs
mosaicplot(us_svy_tab, shade=TRUE)
