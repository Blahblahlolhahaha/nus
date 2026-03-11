liverpool = read.csv("exam/liverpool_2223_season.csv")

aggregate(GF~Venue, data=liverpool, FUN=mean)


student = read.csv("exam/student-mat.csv", sep=";")

tab = table(student[, c("paid","higher")])

chisq.test(tab)$expected
fisher.test(tab)


liverpool$id <- c(1,2)
liverpool

library(lattice)
concrete = read.csv("exam/slump_test.data")
sad = histogram(concrete$Cement, breaks=c(100, seq(200,400, by=25)))
sad


histogram(~ absences | school + sex,
    data = student,
    ylab = "Count",
    main = "Absence by school and gender", as.table = TRUE,
    breaks=seq(0,80, by=5)
)


student$quartile <- findInterval(student$G3, quantile(student$G3))
spineplot(student$absences, as.factor(student$quartile))
