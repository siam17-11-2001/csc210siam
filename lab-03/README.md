# Lab 03: File Processing and Data Management

MD RASIMUZZAMAN SIAM

Creature.java is copied unchanged from lab-01. CSV columns are name,size,age,
without a header. Five initial records are included. Programs 1–3 implement
the core assignment; optional generic Any-CSV support is not included.

## Compile and run

Open Git Bash or a terminal in this lab-03 folder, then run:

```bash
javac Creature.java ProcessCreatureFile.java CreatureRegistry.java CreatureCLI.java
java Creature
java CreatureRegistry
java CreatureCLI read 1
java CreatureCLI create 'name:dragon size:Large age:8'
java CreatureCLI update 2 'name:phoenix size:Medium age:12'
java CreatureCLI delete 3
```

CLI rows are 1-based: row 1 is the first creature. Registry methods use
zero-based Java indexes. Create and update require all three fields, in any
order. Field values cannot contain spaces, commas, or newlines; age is a
nonnegative integer. Windows Command Prompt uses double quotes around fields.

`java ProcessCreatureFile` demonstrates loading an ArrayList, adding a creature,
removing one, changing size and age, and saving. It changes the CSV intentionally.
`java CreatureRegistry` tests count, independent copies, add, modify, save/reload,
and delete; after success it restores the original creature records.
Use a backup CSV if you want to keep the initial five records while experimenting.

Misuse prints usage and exits 1. Missing files and bad indexes report exceptions
on stderr and exit 1. Successful commands exit 0.

## Course smoke test

The assignment references a check.sh in the course repository. That script is
not included in this student repository. When you have the course checkout,
run from lab-03:

```bash
bash /path/to/course-repo/labs/lab-03/check/check.sh
```

## Submission

Email msconroy@bmcc.cuny.edu with subject:
Lab 03: File Processing and Data Management

Include your repository URL:
https://github.com/siam17-11-2001/csc210siam

Also include `Hours spent: ___` with your honest estimate, and verify that
GitHub user matthewscottconroy is a collaborator. Review the code and run it
before submitting so you can explain each program.
