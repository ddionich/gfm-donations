# gfm-recurring — GoFundMe take-home exercise

> **What this repository is:** a take-home coding exercise I completed in November 2024 as part of
> an interview process with **GoFundMe**. It is not a product, a library, or an actively maintained
> project — it is an interview submission, preserved here as a work sample.
>
> The source code is kept **exactly as it was delivered**. Only the documentation in this README has
> been rewritten since, to give the exercise proper context and to be honest about what the
> implementation does and does not get right (see [Known limitations](#known-limitations)).

---

## The exercise

The problem statement was provided by the company. It is summarised here in my own words rather
than reproduced verbatim, since the original brief asked candidates not to publish it.

**In short:** build a command-line tool for a hypothetical GoFundMe "recurring donations" feature.
The tool reads a list of commands, either from a file passed as an argument or from `stdin`, and
prints a monthly summary once the whole input has been consumed.

There are three commands:

| Command | Meaning |
| --- | --- |
| `Add Donor <name> $<limit>` | Register a donor with a monthly recurring donation limit |
| `Add Campaign <name>` | Register a campaign |
| `Donate <donor> <campaign> $<amount>` | Set up a recurring monthly donation |

A `Donate` command that would push a donor over their monthly limit must be **ignored**. The final
summary has a Donors section (total and average donation per donor) and a Campaigns section (total
received per campaign), both in alphabetical order.

The submission was expected to include the implementation, tests, a README covering build/run/test
steps and design rationale, and a single executable named `gfm-recurring` in the project root.

### Example

Input (`input.txt`, included in this repository):

```text
Add Donor Greg $1000
Add Donor Janine $100
Add Campaign SaveTheDogs
Add Campaign HelpTheKids
Donate Greg SaveTheDogs $100
Donate Greg HelpTheKids $200
Donate Janine SaveTheDogs $50
```

Output required by the exercise:

```text
Donors:
Greg: Total: $300 Average: $150
Janine: Total: $50 Average: $50

Campaigns:
HelpTheKids: Total: $200
SaveTheDogs: Total: $150
```

The submitted implementation computes the right numbers but formats and orders them differently —
see [Known limitations](#known-limitations) for the exact output it actually produces.

---

## Requirements

- A **JDK** on your `PATH` (any recent version; the build was developed against JDK 17).
- Nothing else. Gradle itself is provided through the wrapper (`./gradlew`), which downloads the
  Gradle distribution and the project dependencies on first run, so the first build needs network
  access.

The build targets a Java 17 toolchain. If your local JDK is a different version, Gradle will try to
download a matching JDK automatically via the [Foojay toolchain resolver](https://github.com/gradle/foojay-toolchains)
configured in `settings.gradle`.

## Build and run

The `gfm-recurring` script compiles the project on first use (and reuses the resulting JAR
afterwards), so there is no separate build step:

```bash
chmod +x gfm-recurring     # only needed once, if the executable bit was lost
./gfm-recurring input.txt  # read commands from a file
cat input.txt | ./gfm-recurring   # read commands from stdin
```

Both invocations produce the same output.

To build the JAR explicitly instead:

```bash
./gradlew clean build
java -jar build/libs/gfm-donations.jar input.txt
```

## Tests

The test suite uses JUnit 5, `kotlin-test` and MockK, and covers the services, the donation limit
rules and the command parser (16 tests in total):

```bash
./gradlew test
```

The HTML report is written to `build/reports/tests/test/index.html`.

---

## Design and rationale

The brief asked for a solution built on the standard library, with external dependencies limited to
build and test tooling. The main design choices were:

- **Layered structure.** `model` holds the domain entities (`Donor`, `Campaign`, `Donation`),
  `repository` handles persistence behind interfaces, `service` holds the business rules, and
  `utils/CommandProcessor` is the only layer that knows about the CLI's text format. Swapping the
  input format or the storage backend touches one layer each.
- **Repositories behind interfaces.** `Repository<E, ID>` defines the contract and
  `InMemoryRepository` provides the only implementation used here. The intent was to show where a
  real database would plug in without changing any service.
- **A minimal DI container.** `di/DI.kt` is a small service registry with eager and lazy
  registration, wired in `DependencyInitializer`. It keeps constructor injection (and therefore
  mock-based unit tests) possible without pulling in Koin, Dagger or Spring.
- **Domain-specific exceptions.** `DonationLimitExceededException` and `EntityNotFoundException`
  make invalid states explicit rather than failing silently.
- **A launcher script.** `gfm-recurring` builds on demand and then runs the JAR, so the reviewer
  only has to run one command.

The overall goal was to show a structure that could grow — where persistence, validation and I/O
have obvious homes — rather than the shortest program that satisfies the sample input. Given the
size of the actual problem, this is deliberately more structure than the task strictly needs.

## Known limitations

These are real defects in the submitted code, kept here rather than quietly patched, since this
repository is a record of what was delivered.

1. **Output formatting.** The summary lines are missing the colons after `Total` and `Average`. The
   implementation prints `Greg: Total $300 Average $150` where the exercise asks for
   `Greg: Total: $300 Average: $150`.
2. **Ordering.** Donors and campaigns are printed in insertion order, not alphabetical order. With
   the sample input the campaigns therefore come out as `SaveTheDogs` before `HelpTheKids`.
3. **Donations over the limit.** A `Donate` command that exceeds a donor's monthly limit throws
   `DonationLimitExceededException` and aborts the run, whereas the exercise requires it to be
   silently ignored and the run to continue. The same applies to donations referencing an unknown
   donor or campaign.

So the actual output for `input.txt` is:

```text
Donors:
Greg: Total $300 Average $150
Janine: Total $50 Average $50

Campaigns:
SaveTheDogs: Total $150
HelpTheKids: Total $200
```

Other things I would change with more time:

- `DonorService.generateSummaryReport` divides by `donations.size`, which yields `NaN` for a donor
  with no donations.
- `Donation`'s identity is `campaign_donor`, so a second donation from the same donor to the same
  campaign overwrites the first instead of adding to it.
- The command parser indexes into the split line positionally and would fail on malformed input with
  too few tokens; a stricter parse with clear error reporting would be better.
- Monetary amounts are `Double`. `BigDecimal` (or integer cents) is the right choice for money.
- The build declares a `TODO` about trimming dependencies, and there is no CI configuration.

## Project structure

```
gfm-recurring              # launcher script: builds if needed, then runs the JAR
input.txt                  # sample input from the exercise
build.gradle               # Gradle build (Kotlin JVM + application plugin)
src/main/kotlin/com/dionich/gofundme/payments/recurring/
├── Main.kt                # entry point: reads the file or stdin
├── di/                    # minimal service registry and wiring
├── dto/report/            # summary report objects and their rendering
├── exception/             # domain-specific exceptions
├── model/                 # Donor, Campaign, Donation
├── repository/            # repository interfaces + in-memory implementations
├── service/               # business rules, including the donation limit check
└── utils/                 # command parsing and number formatting
src/test/kotlin/...        # JUnit 5 / MockK tests
```
