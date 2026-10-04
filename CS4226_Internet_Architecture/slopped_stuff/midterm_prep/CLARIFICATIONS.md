# Revision Clarifications from Our Q&A

Key points from the questions about Chapter 1 P6 and Chapter 2 Q10, plus the empty-queue formula. Formulas use plain text throughout.

[Practice index](README.md)

## 1. Having arrival and service rates does not make a system M/M/1

**A single server with known rates is not automatically an M/M/1 queue.**

For the course's M/M/1 model, we need Poisson arrivals, independent exponentially distributed service times, a single server, FIFO service and an infinite buffer. Stationary formulas also require `lambda < mu`.

Here:

- `lambda` = arrival rate, in jobs or packets per second.
- `mu = 1 / E[S]` = service rate, where `E[S]` is mean service time.
- `rho = lambda / mu` = utilization for a stable, work-conserving single server.

Defining `mu = 1 / E[S]` is valid even when service times are not exponential. It does **not** justify using the M/M/1 formula `E[W] = 1 / (mu - lambda)`.

### Application to Chapter 1 P6

The question specifies three possible packet lengths on a fixed-rate link. Since `service time = packet length / link rate`, service times take three possible values, rather than following an exponential distribution. Poisson arrivals are also not specified.

Use the given mean system population with **Little's law**, which does not require Poisson arrivals or exponential service. Use stable long-run averages and a consistent system boundary.

```text
Total arrival rate = 300 + 500 = 800 packets/s

Mean A-packet size = 0.25 × 100 + 0.75 × 200
                   = 175 bytes

Mean size of an arbitrary packet
    = (300 × 175 + 500 × 50) / 800
    = 96.875 bytes

E[S] = 96.875 / 100,000
     = 0.00096875 seconds

E[W] = E[L] / lambda
     = 40 / 800
     = 0.05 seconds

E[D] = E[W] - E[S]
     = 0.04903125 seconds
     = 49.03125 ms
```

**Remember:** weight packet sizes by their packet arrival rates, not equally by stream.

### What did the original PDF say?

Problem 4 of the [Written Assignment Sample Answers](../Written%20Assignment%20Sample%20Answers.pdf) also gives arrival rates and packet sizes without assuming Poisson arrivals or exponential service. Its answer explicitly notes that inter-arrival and service times are not necessarily exponentially distributed, then uses Little's law.

Our P6 uses different numbers but tests that same distinction.

## 2. Empty waiting queue is different from empty system

`L` counts all jobs in the system, **including service**. `Q` counts jobs **waiting only**.

| Situation | L | Q |
|---|---|---|
| No jobs anywhere | 0 | 0 |
| One job being served, none waiting | 1 | 0 |
| One being served, one waiting | 2 | 1 |
| One being served, q waiting, where q >= 1 | q + 1 | q |

The correct relationship is `Q = max(L - 1, 0)`.

For a stationary M/M/1 queue:

```text
P(L = n) = (1 - rho) × rho^n, for n = 0, 1, 2, ...
```

For a **positive** waiting count, there must also be one job in service:

```text
P(Q = q) = P(L = q + 1)
         = (1 - rho) × rho^(q + 1), for q >= 1
```

But an empty waiting queue has **two** possible system states:

```text
P(Q = 0) = P(L = 0) + P(L = 1)
         = (1 - rho) + (1 - rho) × rho
         = 1 - rho^2
```

Substituting `q = 0` into `(1 - rho) × rho^(q + 1)` counts only the busy-server case. It misses the completely empty system.

Use `rho` for utilization here. If you write `p`, make sure you do not confuse it with an exit or success probability in another question.

## 3. Utilization, exact population probability and mean time answer different questions

For server 1 in Chapter 2 Q10:

```text
lambda = 2 jobs/s
mu     = 4 jobs/s
rho    = 2 / 4 = 1/2
```

| What is being asked? | Formula | Result for server 1 |
|---|---|---|
| Probability the server is busy: at least one job in its system | `rho = lambda / mu` | `1/2` |
| Probability of exactly one job in its system | `(1 - rho) × rho^1` | `1/4` |
| Mean sojourn time per visit, including waiting and service | `1 / (mu - lambda)` | `1/2 second` |

**Your calculation `2/4` correctly gives utilization.** It includes all busy states: one job, two jobs, three jobs, and so on. It does not give the probability of exactly one job.

### Why does Q10 contain 1/2 × 1/2?

For exactly one job, substitute `n = 1` into the population formula:

```text
P(L1 = 1) = (1 - rho1) × rho1^1
          = (1 - 1/2) × (1/2)^1
          = 1/2 × 1/2
          = 1/4
```

The first half is `1 - rho1`; the second is `rho1^1`. These are **factors in the population formula**, not an attempt to multiply “idle” and “busy” events.

In Q10(e), this is the server-1 factor in the stationary Jackson product form:

```text
P(L1 = 1, L2 = 2, L3 = 0)
    = P(L1 = 1) × P(L2 = 2) × P(L3 = 0)
    = (1/2 × 1/2) × (1/3 × (2/3)^2) × (3/5)
    = 1/45
```

Each node uses its own effective arrival rate and utilization. Multiplying node probabilities here is justified by the stationary Jackson product form; it is not a rule for arbitrary networks.

## 4. Quick checks before choosing a formula

1. **Identify the output:** a time, a mean count, or a probability?
2. **Identify the boundary:** waiting queue only, one server's entire system, or the whole network?
3. **Read the event carefully:** “busy” means at least one job; “exactly one” is a different event.
4. **Check assumptions:** known rates alone do not establish M/M/1.
5. **Check units:** `1 / (mu - lambda)` gives time; probabilities such as `lambda / mu` have no units.

Two expressions can both evaluate to `0.5` and still describe completely different quantities—such as a busy probability of 0.5 and a mean sojourn time of 0.5 seconds.
