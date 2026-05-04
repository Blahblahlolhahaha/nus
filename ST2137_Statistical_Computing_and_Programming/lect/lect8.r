setwd("c:\\Users\\sad\\Documents\\GitHub\\nus\\ST2137_Statistical_Computing_And_Programming\\lect")

antibio <- read.csv("data/antibio.csv")

u_levels <- sort(unique(antibio$type))

boxplot(org ~ type, data = antibio)

aggregate(org ~ type,
    data = antibio,
    FUN = function(x) c(mean = mean(x), sd = sd(x), n = length(x))
)

antibio$type <- factor(antibio$type,
                        levels = u_levels[c(2, 1, 3, 4, 5, 6)])
antibio_lm = lm(org ~ type, data=antibio)

anova(antibio_lm)


r1 <- residuals(antibio_lm)
hist(r1)
qqnorm(r1)
qqline(r1)


summary_out <- anova(antibio_lm)
est_coef <- coef(antibio_lm)
est1 <- unname(est_coef[3])
MSW <- summary_out$`Mean Sq`[2]
df <- summary_out$Df[2]
q1 <- qt(0.025, df, 0, lower.tail = FALSE)
lower_ci <- est1 - q1 * sqrt(MSW * (1/6 + 1/6))
upper_ci <- est1 + q1 * sqrt(MSW * (1 / 6 + 1 / 6))
cat("The 95% CI for the diff. between Enrofloxacin and Control is (",
    format(lower_ci, digits = 3), ",",
    format(upper_ci, digits = 3), ").",
    sep = ""
)

c1 <- c(-1, 0.5, 0.5)
n_vals <- c(6, 6, 6)
L <- sum(c1*est_coef[3:5])
se1 <- sqrt(MSW * sum( c1^2 / n_vals ) )
q1 <- qt(0.025, df, 0, lower.tail = FALSE)
lower_ci <- L - q1*se1
upper_ci <- L + q1*se1
cat("The 95% CI for the diff. between the two groups is (",
format(lower_ci, digits = 2), ",",
format(upper_ci, digits = 2), ").", sep="")

TukeyHSD(aov(antibio_lm), ordered = TRUE)

kruskal.test(antibio$org, antibio$type)

summary_out$"Pr(>F)"
