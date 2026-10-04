# CS4226 Midterm Practice — MCQs and Fill-in-the-Blanks

**28 questions: 14 single-best-answer MCQs and 14 fill-in-the-blank questions**, organized by chapter. The questions follow the format you specified and cover the supplied lectures through Resource Allocation, Tutorials 1–4, and Written Assignment Sample Answers. They are original revision exercises, not an official mock paper or predictions of exam questions.

## Start here

| Chapter | Format | Suggested practice time | Reference |
|---|---|---|---|
| [1. Performance](01-Performance.md) | 4 MCQs + 4 fill-in questions | 20–25 minutes | [Guide](../quick_references/01-Performance.md) |
| [2. Queueing](02-Queueing.md) | 6 MCQs + 6 fill-in questions | 40–50 minutes | [Guide](../quick_references/02-Queueing.md) |
| [3. Resource Allocation](03-Resource_Allocation.md) | 4 MCQs + 4 fill-in questions | 30–40 minutes | [Guide](../quick_references/03-Resource_Allocation.md) |

These time estimates and the question mix are for practice; the actual exam's timing, weighting and MCQ rules were not supplied.

## Instructions

- Choose **exactly one** option for each MCQ.
- Fill-in questions may have several labeled blanks. Give an exact expression/fraction or a numerical answer to **at least 3 significant figures**, unless a word is requested. Units are printed beside numerical blanks.
- No written proofs or essays are required. Use scratch work for calculations and progressive filling.
- Rates use decimal units: 1 Mbps = $10^6$ bits/s; 1 byte = 8 bits.
- $L$ counts jobs in the system including service; $Q$ counts waiting jobs only. $W$ includes service; $D$ is queueing delay only. Bars denote means.
- For M/M/1 questions, assume FIFO, infinite buffer, independent Poisson arrivals and i.i.d. exponential service. For Jackson questions, independent exponential services and independent routing choices are specified. Use stationary formulas only when stable.

Try the questions before opening the keys. To self-check, award one practice point per correct MCQ and per correct blank; this is not an official marking scheme.

## Answer keys — spoilers

[Clarifications from our Q&A](CLARIFICATIONS.md) — plain-text formulas covering model assumptions, empty queues and population probabilities. Includes worked answers for P6 and Q10.

- [Performance key and explanations](solutions/01-Performance.md)
- [Queueing key and explanations](solutions/02-Queueing.md)
- [Resource Allocation key and explanations](solutions/03-Resource_Allocation.md)

## Coverage

| Material | Practice topics |
|---|---|
| Performance lecture; Tutorial 1 | Delay, multiplexing, binomial overload, independence, Bayes, Little's law |
| Queueing lecture/review; Tutorials 2–3 | Exponential/geometric distributions, memorylessness, races, M/M/1, pooling, effective bandwidth, Jackson networks |
| Resource Allocation lecture; Tutorial 4 | Utilities, fairness, demand caps, weighted bottlenecks, capacity optimization |
| Assignment Q1–Q5 | Geometric convention (Q4); weighted fairness (R6); conditional sojourn (Q7); mixed sizes (P6); joint queue events (Q11) |

Original PDFs are linked in the [reference source map](../quick_references/README.md).
