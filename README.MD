# GoDial

## Introduction

GoDial is a Java-based framework for developing spoken dialogue systems(SDS).

It is designed to perform dialogue management tasks, and features are listed as follows:

- easily extensible
- simple configuration
- domain independent
- support for dialogue context switching

## Sample

Below is a sample of the GoDial. Two domains are defined, which are flight-booking and weather-checking.

In the dialogue, the user wants to book a flight ticket. During the process he asks another question about Beijing's weather. The system responds to the user correctly. Then, the dialogue switches back to the flight-booking domain, and the user books a ticket successfully.

Compared with [OpenDial](http://www.opendial-toolkit.net/), the GoDial framework has the following advantages:

- In the OpenDial, the user will have to follow restricted steps designed by the system manager. For example, if you want to book a ticket, you must firstly input the `department`, secondly the `destination`, and thirdly the `date`. You can not **rearrange** or **combine** different steps, such as `On June 8th, I want to fly to Beijing from Shanghai`.
- In the OpenDial, the user cannot process multiple tasks at the same time, i.e., before you have completed a `flight-booking` task, you cannot start an `music-playing` task, that can be very inconvenient.


```
[User]:hello
[System]:Hello, what can i do for you?

[User]:I want to book a flight ticket
[System]:I will help you with MOCK-FLIGHT-BOOKING!
Ok, what is the departure of MOCK-FLIGHT-BOOKING?

[User]:I'd like to fly to Shanghai from Beijing
[System]:Ok, what is the date of MOCK-FLIGHT-BOOKING?

[User]:Excuse me, but what is Beijing's weather?
[System]:I will help you with MOCK-WEATHER-ASKING!
Ok, what is the date of MOCK-WEATHER-ASKING?

[User]:on June 7th
[System]:The weather in {location=Beijing} on {month=June, day=7} is sunny

[User]:ok, I'll fly on June 7th
[System]:I have helped you book a ticket, the price is 20

[User]:thanks
[System]:Hello, what can i do for you?

[User]:bye
```


## Architecture

![arch](https://cloud.githubusercontent.com/assets/6239743/19806419/51ba256a-9d4d-11e6-8f62-1715bd3bdb0e.png)

### Domain

**Domain** is the core component of the spoken dialogue system.A domain is a manager for handling a specified task, for example, flight-booking, weather-checking, etc.

A **domain** is composed of four different parts:

- A **converter** that can convert the user utterance into user actions. In other words, it can serve as the natural language understanding module. For example, when you say `I would like to book a flight ticket to Beijing`, the converter will recognize that the domain that can help you is `flight-booking` and your `destination` is `Beijing`.
- An **executor** that can employ external modules to do some real things, such as use the web service to check the weather, book a ticket, etc.
- A **generator** that can generate system responses. It serve as the natural language generating module. For example, when you ask `Who are you`, the system may say that `I am GoDial, nice to meet you!`.
- A **dialogue structure** that specifies the necessary elements of the task. For a flight-booking domain, the necessary elements are `department`, `destination`, `date`, etc.

### Context

A spoken **context** is the data of a domain. A domain only contains the structured information of a task, for example, a `flight-booking` domain will tell us that we need to fill slots like `destination`, `department`, `date`, but what the destination is, what the department is, and what the date is are stored in a context. If we view a domain as the bone of a man, then the context is the flesh.

### Kernel

The **kernel** is the heart of the system. It is able to load different domains, receive user inputs, switch between different spoken contexts, call external modules, generate system responses, etc.

Every domain should be first registered into the **kernel**, then if a domain is **triggered**, the kernel will generate a spoken context for the domain, and if a task is **completed**, the kernel will destroy the context of the domain.



## Work Flow

![flow](https://cloud.githubusercontent.com/assets/6239743/19806380/04e4d668-9d4d-11e6-8b18-9d5eab496f74.png)

The full work flow are listed as follows:

1. The **kernel** receives a user utterance as the input.
2. **Converters** of several domains will try to understand what the user want to do, but they will give different results.
3. The **kernel** will try to select the most probable one from results in the previous step, and the **domain** to process the task is determined.
4. The **executor** of the selected domain will call external modules to do some things such as querying from internet and return the result.
5. The **generator** of the selected domain will generate a word as the system response.


## Tutorial

You can view sample codes in the `sample` package, configuration files are put in the `domain_confs` folder.

To create a new domain, you just need to call the method `Domain.newInstance(configurationFilePath)`. Then you need to use the `kernel.registerDomain(domain)` to register the created domain into the kernel.

The configuration files are written in the format of `JSON`, and patterns are written in `regexplus`(a modified regular expression). For more detail, you may refer to the source code `utils.RegexUtils`.

By default, the system will employ **DefaultConverter** (which processes utterance by regexplus), **DefaultExecutor** (which simply prints some information on the console), and **DefaultGenerator** (which keeps asking the user to fill the next slot) for all domains.

To make your domain be able to process more complicated tasks, you may need extend the **AbstractConverter**, **AbstractExecutor**, and **AbstractGenerator**, and then call `set(Converter|Executor|Generator)` to plug your extensions into the domain.


## Dependencies

- `commons-lang3-3.5`
- `commons-logging-1.2`
- `log4j-1.2.17`
- `jackson-annotations-2.8.4`
- `jackson-core-2.8.4`
- `jackson-databind-2.8.4`