# CS4226 Midterm Quick Reference

**Scope:** Network Performance → Queueing Models (including supplementary review) → Resource Allocation. Based on the supplied lectures, Tutorials 1–4 and their slides, and Written Assignment Sample Answers. SDN is outside this guide's scope.

**How to use:** Start with the lookup table, then jump to the relevant section. “Derived forms” means useful rearrangements, special cases and short derivations; calculus derivatives are included for capacity optimization. Extensions derived from the course formulas are explicitly marked.

## Chapters

- [Chapter 1: Network Performance](01-Performance.md)
- [Chapter 2: Queueing Models](02-Queueing.md)
- [Chapter 3: Resource Allocation](03-Resource_Allocation.md)

## Fast formula lookup

| Question | Formula / approach | Conditions / reminder |
|---|---|---|
| Transmission / propagation time | $b/R$ / $\ell/v$ | Match bits with bits/s, or bytes with bytes/s |
| Average number / time | $\bar L=\lambda\bar W$, $\bar Q=\lambda\bar D$ | Little's law; use the same system boundary |
| Server utilization | $\rho=\lambda E[S]=\lambda/\mu$ | Stable, work-conserving single server |
| M/M/1 mean system time | $\bar W=1/(\mu-\lambda)$ | Require $\lambda<\mu$ |
| M/M/1 mean queueing time | $\bar D=\rho/(\mu-\lambda)=\lambda/[\mu(\mu-\lambda)]$ | Excludes own service |
| M/M/1 mean counts | $\bar L=\rho/(1-\rho)$; $\bar Q=\rho^2/(1-\rho)$ | $\bar L-\bar Q=\rho$ |
| M/M/1 state probability | $P(L=n)=(1-\rho)\rho^n$ | $n=0,1,\ldots$ |
| Empty system / empty queue | $1-\rho$ / $1-\rho^2$ | Queue empty means $L=0$ or $1$ |
| Exponential waiting time | $P(T>t)=e^{-\lambda t}$; $E[T]=1/\lambda$ | $t\ge0$ |
| First of independent Poisson streams | Mean wait $1/\sum_i\lambda_i$; stream $i$ wins with $\lambda_i/\sum_j\lambda_j$ | Add rates, not mean times |
| Jackson effective rates | $\lambda_i=r_i+\sum_j\lambda_jP_{ji}$ | Include feedback |
| Whole-network mean time | $\bar W_{\rm net}=\sum_i\bar L_i/\Lambda$, $\Lambda=\sum_i r_i$ | Denominator is external arrival rate |
| Weighted max-min fairness | Increase active rates in proportion to $\phi_i$ | Compare normalized rates $x_i/\phi_i$ |
| Minimum-delay capacity split | Spare capacity proportional to $\sqrt{a_i}$ for objective $\sum_i a_i/(\mu_i-\lambda_i)$ | Derived generalization; fixed loads and total capacity |

## Notation and units

| Symbol | Meaning |
|---|---|
| $b,R,\ell,v$ | Packet length, link rate, distance, propagation speed |
| $T,S$ | Inter-arrival time, service time |
| $\lambda,\mu$ | Arrival rate and service rate, in packets/jobs per unit time |
| $\rho$ | Utilization / fraction of time server is busy |
| $L,Q$ | Random number in system (queue + service), number waiting only |
| $W,D$ | Random sojourn time (wait + service), queueing delay only |
| $\bar L,\bar Q,\bar W,\bar D$ | $E[L],E[Q],E[W],E[D]$ |
| $r_i,\lambda_i,P_{ij}$ | External arrival rate, total arrival rate at node $i$, routing probability from $i$ to $j$ |
| $C_r,d_i,x_i,\phi_i$ | Resource capacity, flow demand, allocated rate, positive weight |

The slides sometimes use $L,W$ for averages as well as random variables; this guide uses bars for averages. Packet length is $b$ here to avoid confusing it with system population $L$.

$$
T_i=t_{i+1}-t_i,\qquad W_i=t_i^{\rm departure}-t_i^{\rm arrival}=D_i+S_i,
\qquad \lambda=\frac1{E[T]},\quad\mu=\frac1{E[S]}.
$$

**Unit conversions:** $1\text{ byte}=8\text{ bits}$; $1\text{ ms}=10^{-3}\text{ s}$; Mbps means $10^6$ bits/s. If lengths are in bytes, use a byte/s link rate or convert lengths to bits.

## Last-minute error checklist

- **Identify the random variable:** waiting time, count in a fixed interval, active users, trials until success, or stationary queue population?
- **Check units:** link capacity in bytes/s is not a service rate in packets/s until packet size is accounted for.
- **Check the model:** Little's law is general; $1/(\mu-\lambda)$ requires the appropriate M/M/1 model.
- **Check stability before using stationary formulas:** every node must have $\lambda_i<\mu_i$.
- **Read “queue” versus “system”:** exclude versus include the packet in service.
- **Read “more than” versus “at least”:** $P(L>n)=\rho^{n+1}$, but $P(L\ge n)=\rho^n$.
- **Count repeated visits:** network Little's law divides by external arrivals, not the sum of internal arrival rates.
- **Separate objectives:** equal delay → equal spare capacities; minimum weighted mean delay → square-root spare capacities; weighted fairness → normalized allocated rates.
- **Check all bottlenecks:** saturation alone is insufficient, and demand-satisfied flows may also have bottlenecks.
- **Do not assume a deterministic guarantee from a mean-delay bound:** $E[W]\le\hat W$ does not say every packet finishes within $\hat W$.

## Source map

These links point to the supplied course material. Derived rearrangements and extensions above are algebraic aids, not additional claimed lecture topics.

| Material | Main contributions |
|---|---|
| [01-Performance](../Lecture%20Slides/01-Performance.pdf) | Delay components, statistical multiplexing, Little's law |
| [02-Queueing](../Lecture%20Slides/02-Queueing.pdf) | Probability, exponential/Poisson, M/M/1, effective bandwidth, Jackson networks |
| [02a-review](../Lecture%20Slides/02a-review.pdf) | Notation, assumptions, utilization and system/queue distinction |
| [03-Resource Allocation](../Lecture%20Slides/03-Resource_Allocation.pdf) | Utilities, max-min fairness, bottlenecks, weighted water filling |
| [Tutorial 1 slides](../Tutorial/Tutorial1.pdf) / [questions](../Tutorial/tut01.pdf) | Independence, binomial overload, Bayes, Little's law |
| [Tutorial 2 slides](../Tutorial/tut02-slides.pdf) / [questions](../Tutorial/tut02.pdf) | Memorylessness, merging, derivation of mean populations |
| [Tutorial 3 slides](../Tutorial/tut03-slides.pdf) / [questions](../Tutorial/tut03.pdf) | Exponential races, pooling, equal-delay allocation, feedback network |
| [Tutorial 4 slides](../Tutorial/tut04-slides.pdf) / [questions](../Tutorial/tut04.pdf) | Fairness examples, normalized bottlenecks, delay optimization |
| [Written Assignment Sample Answers](../Written%20Assignment%20Sample%20Answers.pdf) | Q1 geometric; Q2 weighted fairness; Q3 conditional sojourn; Q4 mixed sizes; Q5 joint queue probabilities |
