# Questions JSON Format

## Your JSON File Should Look Like This

Place your JSON file at: `src/main/resources/questions.json`

### Format

```json
[
  {
    "topic_tag": "Signs",
    "difficulty": "Easy",
    "question_en": "In heavy traffic, you see a GIVE WAY sign. What should you do?",
    "question_ar": "في ازدحام مروري، ترى إشارة أعطِ الأفضلية. ماذا يجب أن تفعل؟",
    "question_hi_ur": "heavy traffic mein, aap GIVE WAY sign dekhtay/ dekhti hain. Aap ko kya karna chahiye?",
    "A_en": "Proceed only if the road is clear and it is safe.",
    "B_en": "Slow down and be prepared to stop if necessary.",
    "C_en": "Stop completely at the stop line, then proceed only when it is safe.",
    "D_en": "Increase your following distance and reduce speed.",
    "A_ar": "تابع فقط إذا كان الطريق خاليًا وكان ذلك آمنًا.",
    "B_ar": "خفف السرعة وكن مستعدًا للتوقف إذا لزم الأمر.",
    "C_ar": "توقف تمامًا عند خط التوقف، ثم تابع فقط عندما يكون ذلك آمنًا.",
    "D_ar": "زد مسافة الأمان وقلل السرعة.",
    "A_hi_ur": "Sirf tab chalayen jab road clear ho aur safe ho.",
    "B_hi_ur": "Raftar kam karein aur zarurat par ruknay ke liye tayyar rahain.",
    "C_hi_ur": "Stop line par poori tarah rukain, phir sirf safe honay par chalayen.",
    "D_hi_ur": "Following distance barhayein aur speed kam karein.",
    "correct": "B",
    "explanation_en": "Follow the instruction of the GIVE WAY sign to control traffic safely.",
    "explanation_ar": "اتبع تعليمات إشارة أعطِ الأفضلية لتنظيم الحركة بأمان.",
    "explanation_hi_ur": "GIVE WAY sign ki hidayat par amal karna safety ke liye zaroori hai."
  },
  {
    "topic_tag": "Traffic Rules",
    "difficulty": "Medium",
    "question_en": "What is the maximum speed limit in residential areas?",
    "question_ar": "ما هي السرعة القصوى في المناطق السكنية؟",
    "question_hi_ur": "Residential areas mein maximum speed limit kya hai?",
    "A_en": "40 km/h",
    "B_en": "60 km/h",
    "C_en": "80 km/h",
    "D_en": "100 km/h",
    "A_ar": "40 كم/ساعة",
    "B_ar": "60 كم/ساعة",
    "C_ar": "80 كم/ساعة",
    "D_ar": "100 كم/ساعة",
    "A_hi_ur": "40 km/h",
    "B_hi_ur": "60 km/h",
    "C_hi_ur": "80 km/h",
    "D_hi_ur": "100 km/h",
    "correct": "B",
    "explanation_en": "The speed limit in residential areas is 60 km/h for safety.",
    "explanation_ar": "حد السرعة في المناطق السكنية هو 60 كم/ساعة من أجل السلامة.",
    "explanation_hi_ur": "Safety ke liye residential areas mein speed limit 60 km/h hai."
  }
]
```

## Important Notes

### Field Names (Case Sensitive!)

- **Options must be uppercase**: `A_en`, `B_en`, `C_en`, `D_en` (NOT `a_en`)
- All other fields are lowercase with underscores
- The application now supports this exact format

### Required Fields

All fields are required for each question:

**Basic Info:**
- `topic_tag` - Topic category (e.g., "Signs", "Traffic Rules", "Parking")
- `difficulty` - Difficulty level (e.g., "Easy", "Medium", "Hard")
- `correct` - The correct answer (A, B, C, or D)

**Questions (3 languages):**
- `question_en` - Question in English
- `question_ar` - Question in Arabic
- `question_hi_ur` - Question in Hindi/Urdu

**Options English:**
- `A_en`, `B_en`, `C_en`, `D_en`

**Options Arabic:**
- `A_ar`, `B_ar`, `C_ar`, `D_ar`

**Options Hindi/Urdu:**
- `A_hi_ur`, `B_hi_ur`, `C_hi_ur`, `D_hi_ur`

**Explanations:**
- `explanation_en` - Explanation in English
- `explanation_ar` - Explanation in Arabic
- `explanation_hi_ur` - Explanation in Hindi/Urdu

## How to Add Your 1500 Questions

1. Ensure your JSON file is an **array** (starts with `[` and ends with `]`)
2. Each question is an **object** inside the array
3. Separate questions with commas
4. Save the file as: `src/main/resources/questions.json`
5. Restart the application

## Testing Your JSON

Before adding all 1500 questions, test with just a few:

1. Start with 2-3 questions
2. Run the application
3. Check console for: "Successfully loaded X questions!"
4. If successful, add the rest of your questions

## Common Mistakes to Avoid

❌ **Wrong**: `a_en` (lowercase a)
✅ **Correct**: `A_en` (uppercase A)

❌ **Wrong**: Missing comma between questions
✅ **Correct**: Each question separated by comma

❌ **Wrong**: Missing quotes around strings
✅ **Correct**: All strings in double quotes

❌ **Wrong**: Extra comma after last question
✅ **Correct**: No comma after the last question in array

## Validation

To validate your JSON before using it:
- Use https://jsonlint.com/
- Copy your JSON and click "Validate JSON"
- Fix any errors shown

## After Loading

Once loaded successfully:
- Questions are stored in H2 database
- They persist even after restart
- To reload questions: Delete the `data` folder and restart

## Example Test

Create this small test file first:

**File**: `src/main/resources/questions.json`

```json
[
  {
    "topic_tag": "Test",
    "difficulty": "Easy",
    "question_en": "Test question?",
    "question_ar": "سؤال اختبار؟",
    "question_hi_ur": "Test sawal?",
    "A_en": "Option A",
    "B_en": "Option B",
    "C_en": "Option C",
    "D_en": "Option D",
    "A_ar": "الخيار أ",
    "B_ar": "الخيار ب",
    "C_ar": "الخيار ج",
    "D_ar": "الخيار د",
    "A_hi_ur": "Option A",
    "B_hi_ur": "Option B",
    "C_hi_ur": "Option C",
    "D_hi_ur": "Option D",
    "correct": "A",
    "explanation_en": "Test explanation",
    "explanation_ar": "شرح الاختبار",
    "explanation_hi_ur": "Test explanation"
  }
]
```

If this loads successfully, replace it with your full 1500 questions.
