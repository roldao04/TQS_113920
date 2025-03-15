# Lab 2
## 2.1
### c)
```
Unnecessary stubbings detected.
Clean & maintainable test code requires zero unnecessary code.
Following stubbings are unnecessary (click to navigate to relevant line of code):
  1. -> at StocksPortfolioTest.testTotalValue(StocksPortfolioTest.java:40)
  2. -> at StocksPortfolioTest.testTotalValue(StocksPortfolioTest.java:41)
  3. -> at StocksPortfolioTest.testTotalValue(StocksPortfolioTest.java:42)
Please remove unnecessary stubbings or use 'lenient' strictness. More info: javadoc for UnnecessaryStubbingException class.
```
### e) // f)
AI did a good implementation of the code. Tested with edge cases as well.

## 2.2
### b)
Mocking is essential in this scenario because we only test JSON deserialization so the actual HTTP request behavior is irrelevant to this test. Also API calls have a cost, mocking eliminates the need for real API calls.

## 2.3
### e)
It works as intended. Not working with internet off and working with it on.
### f)
| Command                        | Runs Unit Tests? | Runs Integration Tests? | Builds JAR/WAR? | Installs in Local Repo? |
|--------------------------------|-----------------|----------------------|---------------|---------------------|
| `mvn test`                     | ✅ Yes          | ❌ No               | ❌ No        | ❌ No              |
| `mvn package`                  | ✅ Yes          | ❌ No               | ✅ Yes       | ❌ No              |
| `mvn package -DskipTests=true` | ❌ No           | ❌ No               | ✅ Yes       | ❌ No              |
| `mvn failsafe:integration-test`| ❌ No           | ✅ Yes               | ❌ No        | ❌ No              |
| `mvn install`                  | ✅ Yes          | ✅ Yes               | ✅ Yes       | ✅ Yes              |

- mvn test → Runs unit tests only, no packaging.
- mvn package → Runs unit tests, then builds JAR/WAR.
- mvn package -DskipTests=true → Skips all tests, only builds JAR/WAR.
- mvn failsafe:integration-test → Runs only integration tests.
- mvn install → Runs all tests, builds, and installs in local repo.