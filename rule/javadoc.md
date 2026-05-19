# Java Documentation Standard
> **Context:** All source code developed within the project (excluding external libraries or generated code).
> **Scope:** All classes, interfaces, enums, and methods under the project namespace.
> **Language:** English (US).

## 1. Class Documentation (File Header)
Every Java file starts with a simple attribution block followed by the Class Javadoc.

### 1.1 The Schema
```text
/*
 * Created by Truong Ngo ([YEAR]).
 */
package [package.name];

import ...;

/**
 * [SHORT_SUMMARY_NOUN_PHRASE].
 * <p>
 * [DETAILED_DESCRIPTION] - Explain the main purpose, key features.
 * Use HTML tags like {@code <ul>} and {@code <li>} for lists.
 * </p>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 * <li>[Feature 1]</li>
 * <li>[Feature 2]</li>
 * </ul>
 *
 * <p>This class is [Thread-Safety Status] and [intended for inheritance or final].</p>
 *
 * @author Truong Ngo
 * @version [Current_Project_Version]
 * @see [RelatedClass]
 * @since [Since_Version]
 */
public [final] class [ClassName] { ... }
```

### 1.2 Filling Guidelines
* **Header:** Use the simple format: `/* Created by Truong Ngo (2026). */` before the package declaration.
* **Summary:** Start with a Noun Phrase (e.g., `Manager for...`, `Controller handling...`).
* **Metadata:**
  * `@author`: Always `Truong Ngo`.
  * `@version`: Matches the project version.

---

## 2. Code Organization (Section Separators)
Use section separators to visually group related members. Do not use "Box Style".

### 2.1 The Style
Do not use "Box Style" (closed borders). Use the open-bar style for cleaner visuals.

```text
    /*
     * -----------------------------------------------------------------------------------------------------------------
     * SECTION_NAME (UPPERCASE)
     * -----------------------------------------------------------------------------------------------------------------
     */
```

### 2.1 The Style
Do not use "Box Style" (closed borders). Use the open-bar style for cleaner visuals.

---

## 3. Fields & Constants Documentation
Document public/protected fields and constants clearly. Private fields can use minimal documentation.

### 3.1 Constants (static final)
```text
    /**
     * [DESCRIPTION] (Unit of measurement if applicable).
     * @see [RelatedConstant]
     */
    public static final int DEFAULT_TIMEOUT_MS = 5000;
```

### 3.2 Fields (Properties)
```text
    /**
     * [DESCRIPTION].
     * <p>
     * [OPTIONAL: STATE constraints, e.g., "Always non-null after init"].
     * </p>
     */
    private final String name;
```

---

## 4. Constructors & Static Blocks

### 4.1 Constructors
Constructors follow the method style but focus on "Initialization".

```text
    /**
     * Constructs a new [ClassName] with [key_attributes].
     *
     * @param [paramName] [description]
     * @throws [Exception] if validation fails (e.g., null check)
     */
    public ClassName(...) { ... }
```

**Special Case: Utility Classes**
For `final` utility classes, document the private constructor explicitly:
```text
    /**
     * Prevents instantiation of this utility class.
     * @throws UnsupportedOperationException always
     */
    private ClassUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
```

### 4.2 Static Initializer Blocks
Since Javadoc ignores static blocks, use a standard block comment `/* ... */` immediately before the block.

```text
    /*
     * Initializes the static cache/mapping.
     * Loads configuration from [Source] safely.
     */
    static {
        // ... logic
    }
```

---

## 5. Public Method Documentation (STRICT)
Every public method **MUST** follow this extended schema.

### 5.1 The Schema
```text
/**
 * [VERB_3RD_PERSON] [THE_SUBJECT] [CONTEXT/CONDITION].
 * <p>
 * [OPTIONAL: DETAILED EXPLANATION] - Implementation details, thread-safety,
 * or edge case handling.
 * </p>
 *
 * <pre>{@code
 * // [OPTIONAL: EXAMPLE USAGE - Recommended for complex logic]
 * // The '{@code}' tag is REQUIRED inside <pre> to handle Generics correctly
 * List<String> result = ClassUtils.method(args);
 * }</pre>
 *
 * @param <T> [description_of_type] (REQUIRED if method defines a Type Parameter)
 * @param [paramName] the [subject] ([NULLABILITY_MARKER]; [SPECIFIC_CONSTRAINTS])
 * @return [RETURN_DESCRIPTION] - Explicitly state behavior for {@code null} results
 * @throws [ExceptionType] if [specific error condition occurs]
 * @see [RelatedClass#method]
 * @since [Version_Number]
 * @deprecated [Reason] (Use {@link #newMethod()} instead)
 */
```

### 5.2 Filling Guidelines
* **Verbs:** Start with **3rd person singular** (e.g., `Checks`, `Calculates`). **DO NOT** use "Check", "Return".
* **Formatting:** Wrap keywords in code tags: `{@code null}`, `{@code true}`, `{@code String}`.
* **Code Blocks:** Always use `<pre>{@code ... }</pre>`.
* **Nullability:** Explicitly state `(maybe {@code null})` or `Returns {@code null} if...`.
* **Generics:** Include `@param <T>` if applicable.

### 5.3 Reference Example (The Gold Standard)
```text
/**
 * Instantiates the specified class using its default constructor.
 * <p>
 * This wrapper handles accessibility checks and converts checked reflection
 * exceptions into unchecked {@link IllegalStateException}.
 * </p>
 *
 * <pre>{@code
 * // Example: Create a new instance of ArrayList
 * List<String> list = ClassUtils.instantiateClass(ArrayList.class);
 * }</pre>
 *
 * @param <T>   the type of the class
 * @param clazz the class to instantiate (must not be {@code null})
 * @return a new instance of the class (never {@code null})
 * @throws IllegalArgumentException if the class is an interface
 * @throws IllegalStateException    if the class has no default constructor
 * @see java.lang.reflect.Constructor#newInstance()
 * @since 1.0.0
 */
public static <T> T instantiateClass(Class<T> clazz) { ... }
```

---

## 6. Private / Internal Method Documentation
Private helper methods follow a relaxed documentation standard.

### 6.1 The Rules
1.  **Format:** Use Javadoc block `/** ... */`.
2.  **No Tags:** Do **NOT** use `@param`, `@return` unless necessary.
3.  **Focus:** Explain **"Why"** and **"Assumptions"**.

### 6.2 Example
```text
/**
 * Helper for {@link #resolveProxy(Object)}.
 * Peels off Hibernate proxies to reveal the real entity class.
 * Assumes the input object is definitely a Hibernate proxy.
 */
private static Class<?> resolveHibernateProxy(Object o) { ... }
```

## 7. Inline Documentation (Inside Methods)
Comments inside the method body explain the **logic flow** and **"Why"** decisions, not the obvious syntax.

### 7.1 The Rules
1.  **Language:** English only.
2.  **Style:** Use `//` for all inline comments. Do not use `/* ... */` block styles inside methods (reserved for commenting out code).
3.  **Placement:** Place the comment **on the line above** the code it describes, indented to match the code.
4.  **Steps:** Number the logical steps (1, 2, 3...) if the algorithm is sequential/long.
5.  **Keywords:** Use uppercase keywords for emphasis:
* `// NOTE:` For important context or limitations.
* `// TODO:` For future work (followed by Jira ticket if applicable).
* `// FIXME:` For known bugs/hacks.

### 7.2 Example
```text
// 1. Check cache first
Object cached = cache.get(key);
if (cached != null) {
    return cached;
}

// 2. Fallback to database
// NOTE: This operation is expensive, ensure timeout is set.
Object result = database.load(key);