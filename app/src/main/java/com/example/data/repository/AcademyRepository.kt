package com.example.data.repository

import com.example.data.database.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

// Model definitions for curriculum & foundations
data class CurriculumTopic(
    val titleEnglish: String,
    val titleHindi: String,
    val descriptionEnglish: String,
    val descriptionHindi: String,
    val contentEnglish: String,
    val contentHindi: String
)

data class StudyClass(
    val className: String, // Nursery, LKG, UKG, Class 1, etc.
    val subjects: Map<String, List<CurriculumTopic>>
)

class AcademyRepository(private val dao: TeacherBaseDao) {

    // --- Database Operations ---
    val allLessonPlans: Flow<List<LessonPlan>> = dao.getAllLessonPlans()
    fun getLessonPlansForClass(className: String): Flow<List<LessonPlan>> = dao.getLessonPlansForClass(className)
    suspend fun insertLessonPlan(plan: LessonPlan) = dao.insertLessonPlan(plan)
    suspend fun deleteLessonPlan(plan: LessonPlan) = dao.deleteLessonPlan(plan)

    val allQuizAttempts: Flow<List<QuizAttempt>> = dao.getAllQuizAttempts()
    suspend fun insertQuizAttempt(attempt: QuizAttempt) = dao.insertQuizAttempt(attempt)

    suspend fun getProgressForDate(dateStr: String): RoadmapProgress? = dao.getProgressForDate(dateStr)
    suspend fun saveProgress(progress: RoadmapProgress) = dao.insertProgress(progress)
    val recentProgress: Flow<List<RoadmapProgress>> = dao.getRecentProgress()

    val allNotes: Flow<List<TeacherNote>> = dao.getAllNotes()
    suspend fun insertNote(note: TeacherNote) = dao.insertNote(note)
    suspend fun deleteNoteById(id: Int) = dao.deleteNoteById(id)

    val allStudentReports: Flow<List<StudentReport>> = dao.getAllStudentReports()
    suspend fun insertStudentReport(report: StudentReport) = dao.insertStudentReport(report)
    suspend fun deleteStudentReportById(id: Int) = dao.deleteStudentReportById(id)

    val allWorksheets: Flow<List<CustomWorksheet>> = dao.getAllWorksheets()
    suspend fun insertWorksheet(sheet: CustomWorksheet) = dao.insertWorksheet(sheet)
    suspend fun deleteWorksheetById(id: Int) = dao.deleteWorksheetById(id)


    // --- CURRICULUM SYLLABUS DATA ---
    val curriculumSyllabus: List<StudyClass> = listOf(
        StudyClass("Nursery", mapOf(
            "English" to listOf(
                CurriculumTopic("Alphabet Recognition", "वर्णमाला पहचान", "Big bold letters (A-Z) matching exercises.", "बड़े अक्षर A से Z और उनका मिलान।", "A for Apple, B for Ball... Trace dotted lines. High contrast letter cards are recommended.", "बिंदुदार रेखाओं पर ट्रेसिंग करना। वर्णों के चित्र पहचानना।"),
                CurriculumTopic("Phonics Intro", "ध्वनि परिचय", "Learn the letter sounds (e.g. A says 'ah').", "प्रत्येक अक्षर की मूल ध्वनि (ध्वनि विज्ञान)।", "A says /æ/ as in Apple. B says /b/ as in Ball. Use sound imitation activities.", "A की आवाज 'ऐ', B की आवाज 'ब' होती है। ध्वनि अनुकरण खेल।")
            ),
            "Mathematics" to listOf(
                CurriculumTopic("Counting 1-10", "गिनती 1-10", "Recognize and speak numbers 1 to 10.", "संख्याएं 1-10 की मौखिक व विजुअल पहचान।", "Count toys, claps, or colorful beads. Trace numbers 1, 2, 3...", "खिलौनों, तालियों, या रंग-बिरंगे मोतियों को गिनना। अंक लेखन अभ्यास।")
            ),
            "Hindi" to listOf(
                CurriculumTopic("Swar Parichay", "स्वर परिचय", "Identify swar like अ, आ, इ, ई.", "अ, आ, इ, ई आदि स्वरों की पहचान।", "Sing Hindi rhymes focusing on swar. Highlight the symbols visually.", "स्वरों पर आधारित सरल बालगीत गाना। चित्र देखकर प्रथम अक्षर बताना।")
            )
        )),
        StudyClass("LKG", mapOf(
            "English" to listOf(
                CurriculumTopic("Capital & Small letters", "बड़े और छोटे अक्षर", "Writing capital A-Z and introducing small letters a-z.", "शारीरिक रूप से अक्षरों को लिखना।", "Practice notebook writing on four-lined grids. Focus on holding pencils correctly.", "चार लाइनों वाली कॉपी में लिखने का अभ्यास। पेंसिल पकड़ने का तरीका।")
            ),
            "Mathematics" to listOf(
                CurriculumTopic("Counting & Shapes", "गिनती व आकार", "Count numbers 1-20, shapes like Circle, Square.", "संख्या 1-20 एवं वृत्त, वर्ग की पहचान।", "Draw shapes in sand. Count aloud while tossing soft balls.", "रेत में आकृतियां बनाना। गेंद उछालते हुए गिनती गिनना।")
            )
        )),
        StudyClass("UKG", mapOf(
            "English" to listOf(
                CurriculumTopic("Three Letter Words", "तीन अक्षरों वाले शब्द", "CVC Words like Cat, Hen, Pin, Dog, Bug.", "व्यंजन-स्वर-व्यंजन के संयोंग से बने शब्द।", "Sound blending: c-a-cat. Read simple sentence patterns like 'A cat sat on a mat.'", "c-a-t = cat जैसी ध्वनियों को जोड़ना। 'A cat sat...' जैसे वाक्य।")
            ),
            "Mathematics" to listOf(
                CurriculumTopic("Addition Intro", "जोड़ का परिचय", "Basic single-digit addition using objects.", "वस्तुओं की सहायता से एक अंक का जोड़।", "2 apples + 1 apple = 3 apples. Use tally marks on the drawing canvas.", "2 सेब + 1 सेब = 3 सेब। चित्रों या रेखाओं का उपयोग कर जोड़ना।")
            )
        )),
        StudyClass("Class 1", mapOf(
            "Mathematics" to listOf(
                CurriculumTopic("Addition & Subtraction", "जोड़ एवं घटाव", "Single and double digit equations without carryover.", "बिना हासिल वाले दो अंकों के जोड़-घटाव।", "Perform column sum. Let children draw circles, check totals manually.", "कॉलम जोड़ अभ्यास। घेरे बनाकर या हाथ की उंगलियों पर हल करना।")
            ),
            "Science / EVS" to listOf(
                CurriculumTopic("Living Things", "सजीव और निर्जीव", "Understanding plants, animals, objects.", "पेड़-पौधे, जानवर और निर्जीव वस्तुओं की समझ।", "Explore garden: Plants grow and breathe. Stones do not.", "बगीचे का भ्रमण: पौधे सांस लेते हैं और बढ़ते हैं। पत्थर स्थिर हैं।")
            )
        )),
        StudyClass("Class 2", mapOf(
            "Mathematics" to listOf(
                CurriculumTopic("Basic Multiplication", "गुणा की समझ", "Multiplication as repeated addition. Tables 2 to 10.", "बार-बार जोड़ के रूप में गुणा। पहाड़े 2 से 10।", "3 groups of 2 stars is 3 x 2 = 6. Interactive counting game.", "दो-दो सितारों का तीन समूह गुणा को दिखाता है।")
            ),
            "Social Science" to listOf(
                CurriculumTopic("Our Neighborhood", "हमारा पड़ोस", "Learn about post office, hospital, bank.", "पड़ोस में स्थित मुख्य संस्थाएं।", "Roleplay: Visit Doctor, Postman, Police Officer.", "नाटक: अस्पताल, बैंक, और डाकघर की भूमिका समझाना।")
            )
        )),
        StudyClass("Class 3", mapOf(
            "English" to listOf(
                CurriculumTopic("Nouns & Pronouns", "संज्ञा और सर्वनाम", "Learn parts of speech with matching exercises.", "संज्ञा और सर्वनाम के भेद और वाक्य प्रयोग।", "Name of person, place, or thing. Replace with He/She/It.", "किसी व्यक्ति, वस्तु या स्थान का नाम। उसके स्थान पर वह/तुम का प्रयोग।")
            ),
            "Science" to listOf(
                CurriculumTopic("Water Cycle", "जल चक्र", "Evaporation, Condensation, Precipitation.", "वाष्पीकरण, संघनन एवं वर्षा की प्रक्रिया।", "Experiment: Hot water bowl covered with plate to show steam droplets.", "प्रयोग: गुनगुने पानी की कटोरी को ढककर बूंदों को बनते दिखाना।")
            )
        )),
        StudyClass("Class 4", mapOf(
            "Science" to listOf(
                CurriculumTopic("Photosynthesis", "प्रकाश संश्लेषण", "How green plants make food using sunlight.", "सूर्य के प्रकाश से पौधों द्वारा भोजन बनाना।", "Learn Carbon dioxide + Water -> Glucose + Oxygen. Detail chlorophyll.", "क्लोरोफिल का कार्य, पत्तियों को पौधे की रसोई कहना।")
            )
        )),
        StudyClass("Class 5", mapOf(
            "Science" to listOf(
                CurriculumTopic("Solar System", "सौर मंडल", "The Sun, eight planets, moons, orbits.", "सूर्य, आठ ग्रह, उपग्रह और सूर्य की परिक्रमा।", "Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune.", "बुध, शुक्र, पृथ्वी, मंगल, बृहस्पति, शनि, अरुण, वरुण के रोचक तथ्य।")
            ),
            "Mathematics" to listOf(
                CurriculumTopic("Fractions & Decimals", "भिन्न और दशमलव", "Numerator, denominator, decimal notation.", "अंश, हर और दशमलव संख्याओं की अवधारणा।", "Slice visual pizza to explain half, quarter, and sharing.", "पिज़्ज़ा स्लाइस के द्वारा 1/2, 1/4 और हिस्सेदारी को समझाना।")
            )
        ))
    )


    // --- TEACHER TRAINING COURSES ---
    val teacherTrainingModules: List<CurriculumTopic> = listOf(
        CurriculumTopic(
            "1. Child Psychology", "1. बाल मनोविज्ञान",
            "Understand how children think, interact, and behave based on their attention span and intrinsic motivation.",
            "समझें कि बच्चे अपने ध्यान केंद्रित करने की अवधि और प्रेरणा के आधार पर कैसे सोचते और व्यवहार करते हैं।",
            "**Key Concepts:**\n" +
                    "- **Attention Span:** A child's average attention span is 2-5 minutes per year of age (e.g., LKG child: 8-15 mins max).\n" +
                    "- **Motivation:** Positive reinforcement works 10x better than negative scolding.\n" +
                    "- **Active Engagement:** Toddlers learn via sensory inputs. Use touch, see, feel, and hear methods.\n" +
                    "- **Behavior Management:** Redirect negative outbursts gently instead of confrontational discipline.",
            "**मुख्य सिद्धांत:**\n" +
                    "- **ध्यान अवधि (Attention Span):** बच्चे की औसत ध्यान अवधि उम्र प्रति वर्ष 2-5 मिनट होती है। नर्सरी के बच्चे अधिकतम 8-10 मिनट टिक सकते हैं।\n" +
                    "- **सकारात्मक प्रोत्साहन:** डांटने की तुलना में प्रशंसा करने से बच्चे 10 गुना तेजी से नियम सीखते हैं।\n" +
                    "- **संवेदी अधिगम:** बच्चे स्पर्श, आवाज और विजुअल से सीखते हैं। खिलौने एवं चित्रों का भरपूर उपयोग करें।"
        ),
        CurriculumTopic(
            "2. Interactive Teaching Skills", "2. शिक्षण कौशल",
            "Master questioning techniques, explanation frameworks, and verifying if students are learning.",
            "प्रश्नोत्तरी तकनीकों, व्याख्या ढांचों और छात्रों की समझ की पुष्टि करने में विशेषज्ञता प्राप्त करें।",
            "**Action Plan:**\n" +
                    "- **The 3-Step Explain:** Introduce visually first, declare the name second, find real-world examples third.\n" +
                    "- **Active Questioning:** Instead of asking 'Did you understand?', ask specific questions: 'Sohan, what color is the shape?'\n" +
                    "- **Checking Understanding:** Use thumbs up/thumbs down signs, or small slate-board drawing responses.",
            "**कार्य योजना:**\n" +
                    "- **3-चरण व्याख्या:** पहले दृश्य दिखाएं, फिर नाम बताएं, फिर वास्तविक दुनिया से उदाहरण ढूंढें।\n" +
                    "- **सक्रिय प्रश्नोत्तरी:** यह पूछने के बजाय 'क्या समझे?', विशिष्ट प्रश्न पूछें: 'सोहन, इस त्रिभुज में कितनी भुजाएं हैं?'\n" +
                    "- **सीखने का सत्यापन:** अंगूठा ऊपर/नीचे करवाकर सहमति लें, या स्लेट पर तुरंत चित्र बनवा लें।"
        ),
        CurriculumTopic(
            "3. Classroom Management", "3. कक्षा का संचालन",
            "Keep high engagement, establish healthy routines, manage noise, and coordinate parent communication.",
            "सक्रियता बनाए रखें, कक्षा दिनचर्या स्थापित करें, शोर प्रबंधित करें और अभिभावक बैठकें संचालित करें।",
            "**Core Routines:**\n" +
                    "- **Clap Claps Pattern:** When classroom is loud, tap a rhythm: 'Clap-clap-clapclapclap'. The kids must repeat it back and silent down.\n" +
                    "- **Transition Time:** Sing a 1-minute song when moving from writing to cleaning up toys.\n" +
                    "- **Parent Connect:** Share positive daily growth moments. Do not complain directly; frame issues as: 'Together we can improve (X)...'",
            "**मुख्य नियम:**\n" +
                    "- **ताली पैटर्न (Clap Claps):** जब कक्षा में शोर अधिक हो, तो शिक्षक एक विशिष्ट लय में ताली बजाए, बच्चे दोहराकर चुप हो जाएंगे।\n" +
                    "- **सकारात्मक डायरी नोट:** अभिभावक को केवल शिकायत न भेजें; बच्चे की एक अच्छी बात लिखकर सुधार का सुझाव दें।"
        ),
        CurriculumTopic(
            "4. Play-Way & Activity Methods", "4. खेल और गतिविधि आधारित विधि",
            "Explore storytelling, physical movement learning, hands-on experiential education.",
            "कहानी कहने, शारीरिक गतिविधि सीखने और व्यावहारिक शिक्षा का अन्वेषण करें।",
            "**Major Methods:**\n" +
                    "- **Play-Way Method:** Roll giant dice on carpets to count steps. Jump on paper circles printed with letters.\n" +
                    "- **Story Integration:** Introduce math numbers as characters (e.g., 'Number 2 is a beautiful crawling swan...').\n" +
                    "- **Experiential Exploration:** Fetch actual leaves from gardens to count veins instead of viewing textbook drawings.",
            "**प्रमुख शिक्षण विधियां:**\n" +
                    "- **खेल विधि:** फर्श पर बड़ा पासा फेंककर कदम गिनना। वर्णमाला वाले गोल कागजों पर कूदना।\n" +
                    "- **कहानी आधारित परिचय:** गणितीय अंकों को पात्रों की तरह बताना (उदा. 'अंक 2 एक सुंदर रेंगता हुआ बत्तख है...')।\n" +
                    "- **व्यावहारिक प्रयोग:** विज्ञान की किताब के चित्र के बजाय सीधे बगीचे से पत्ती लाकर शिराओं को गिनना।"
        ),
        CurriculumTopic(
            "5. Lesson Planning Templates", "5. पाठ योजना नियम",
            "Discover daily, weekly, and monthly lesson outlines for preschool and primary students.",
            "पूर्व-प्राथमिक और प्राथमिक छात्रों के लिए दैनिक, साप्ताहिक और मासिक पाठ योजनाएं प्राप्त करें।",
            "**Standard Template:**\n" +
                    "- **Warm-up (5 mins):** Short kinetic dance or physical stretch.\n" +
                    "- **Concept Intro (10 mins):** Visual model or toy exposition.\n" +
                    "- **Guided Practice (15 mins):** Complete slate exercises together.\n" +
                    "- **Independent Creative Play (10 mins):** Drawing, color blocks, worksheets.\n" +
                    "- **Wrap-up Evaluation (5 mins):** Ask 3 random check questions.",
            "**मानक समय वितरण:**\n" +
                    "- **वार्म-अप (5 मिनट):** सरल कसरत या छोटा सा नृत्य गीत।\n" +
                    "- **अवधारणा परिचय (10 मिनट):** वास्तविक खिलौनों या बड़े चार्ट का विवरण।\n" +
                    "- **सामूहिक अभ्यास (15 मिनट):** श्यामपट्ट या स्लेट पर मिलकर लिखना।\n" +
                    "- **स्वतंत्र गतिविधि (10 मिनट):** ड्राइंग शीट, कलरिंग या वर्कशीट हल करना।\n" +
                    "- **चर्चा और छुट्टी (5 मिनट):** 3 यादृच्छिक प्रश्न पूछना।"
        )
    )


    // --- HINDI FOUNDATION (Zero to Hero) ---
    val hindiFoundationLessons = listOf(
        CurriculumTopic("Varnamala Intro", "वर्णमाला परिचय", "Hindi alphabet contains 13 Swar (vowels) and 33 Vyanjan (consonants).", "हिंदी वर्णमाला में 13 स्वर और 33 व्यंजन होते हैं।", "Swar: अ, आ, इ, ई, उ, ऊ, ऋ, ए, ऐ, ओ, औ, अं, अः\n\nVyanjan: क, ख, ग, घ, ङ | च, छ, ज, झ, ञ ...", "स्वर (Swar): अ, आ, इ, ई, उ, ऊ, ऋ, ए, ऐ, ओ, औ, अं, अः\n\nव्यंजन (Vyanjan): क, ख, ग, घ, ङ ..."),
        CurriculumTopic("Matra Chart", "मात्रा ज्ञान", "Vowels transform into symbols called Matra.", "स्वर जब व्यंजन से जुड़ते हैं, तो वे मात्रा चिन्ह में बदल जाते हैं।", "क + आ = का, क + इ = कि, क + ई = की, क + उ = कु, क + ऊ = कू", "आ (ा) -> काला, इ (ि) -> दिन, ई (ी) -> पानी, उ (ु) -> पुल, ऊ (ू) -> फूल"),
        CurriculumTopic("Word & Sentence Building", "शब्द एवं वाक्य रचना", "Combining characters to form words, then combining words for simple sentences.", "बिना मात्रा और मात्रा वाले दो-तीन अक्षरों के शब्दों से वाक्यों का निर्माण।", "Two Letter: न + ल = नल, घ + र = घर.\nThree Letter: क + म + ल = कमल.\nSentence: घर चल। जल भर।", "दो अक्षर के शब्द: घर, फल, बस।\nतीन अक्षर के शब्द: कलम, बत्तख।\nसरल वाक्य: 'अमन बस पर चढ़। राम घर चल।'"),
        CurriculumTopic("Moral Stories", "नैतिक कहानियां", "Easy reading Hindi stories for elementary levels.", "ज्ञानवर्धक और सीखने योग्य छोटी कहानियां।", "Story: 'चालक लोमड़ी' - एकता में बल है। Read aloud with students using voice expressions.", "कहानी: 'प्यासा कौवा' - परिश्रम का फल मीठा होता है। कौवे ने घड़े में कंकड़ डाले, पानी ऊपर आया।")
    )


    // --- ENGLISH FOUNDATION ---
    val englishFoundationLessons = listOf(
        CurriculumTopic("Phonics & Alphabet sounds", "ध्वनि और अक्षर ध्वनि", "Learn single letter sounds to blend words fluently.", "अक्षरों के ध्वनि उच्चारण से स्पेलिंग पढ़ना सीखें।", "Letter Sound blendings:\na = /æ/ (apple), b = /b/ (boy), c = /k/ (cat), d = /d/ (dog).", "A की ध्वनि 'ऐ' (Apple), B की ध्वनि 'ब' (Bat), C की ध्वनि 'क' (Cup)।"),
        CurriculumTopic("Sight Words Basics", "साइट वर्ड्स सूची", "Recognize high-frequency words instantly without spelling rules.", "वाक्य निर्माण में बार-बार आने वाले शब्दों को देखकर पहचानें।", "Grade 1 Sight Words: the, of, and, a, to, in, is, you, that, it, he, was, for, on, are, with.", "मुख्य साइट वर्ड्स: the, is, on, my, it, has, we, down, visual memory drilling is best."),
        CurriculumTopic("Basic English Grammar", "व्याकरण आधार", "Start with Nouns (naming), Pronouns, and Action Verbs.", "नाम बताने वाले शब्दों (संज्ञा) और क्रियाओं की पहचान।", "Nouns: cat, Delhi, table. Pronouns: I, we, they, he, she. Verbs: run, jump, write.", "संज्ञा (Nouns): बिल्ली, दिल्ली, मेज़। क्रिया (Verbs): दौड़ना, लिखना, कूदना।"),
        CurriculumTopic("Simple Conversations", "सरल बातचीत अभ्यास", "Basic introductions and daily dialog sheets for pupils.", "सवालों और जवाबों का बुनियादी अभ्यास।", "Teacher: 'What is your name?'\nStudent: 'My name is Rahul.'\nTeacher: 'How old are you?'\nStudent: 'I am six years old.'", "शिक्षिका: 'तुम्हारा नाम क्या है?'\nछात्र: 'मेरा नाम राहुल है।'\nशिक्षिका: 'तुम किस कक्षा में पढ़ते हो?'\nछात्र: 'मैं पहली कक्षा में पढ़ता हूँ।'")
    )


    // --- MATHEMATICS FOUNDATION ---
    val mathFoundationLessons = listOf(
        CurriculumTopic("Number Patterns", "संख्या पैटर्न", "Ascending, descending, and skipped numbers.", "बढ़ते-घटते क्रम, और लुप्त संख्याओं का अभ्यास।", "Ascending: 1 -> 5 -> 10.\nDescending: 10 -> 8 -> 6.\nSkip Counting: 2, 4, 6, 8, 10.", "बढ़ता क्रम: छोटे से बड़ा (3, 6, 9)।\nघटता क्रम: बड़े से छोटा (9, 7, 5)।\nसम संख्या उछल-गिनती।"),
        CurriculumTopic("Vedic Multiplication Tricks", "वैदिक गणित गुणा तरकीब", "Multiply double-digit numbers instantly using vertical-crosswise method.", "ऊर्ध्वतिर्यग्भ्याम् विधि से दो अंकों का त्वरित गुणा।", "Example for 21 x 22:\n1. Unit digit multiply (1 x 2 = 2)\n2. Cross multiply & sum (2x2 + 1x2 = 6)\n3. Tens digit multiply (2 x 2 = 4)\nResult = 462! Zero carryover pain.", "21 x 22 का वैदिक गुणा:\n1. इकाइयों का सीधा गुणा (1 x 2 = 2)\n2. तिरछा गुणा कर उन्हें जोड़ें (2x2 + 1x2 = 6)\n3. दहाई का सीधा गुणा (2 x 2 = 4)\nउत्तर = 462!"),
        CurriculumTopic("Concept of Fractions", "भिन्न की अवधारणा", "Visualizing parts of a whole with shapes.", "किसी पूरी वस्तु के भागों को चित्र के ज़रिए पहचानना।", "1/2 means half of a circle. 1/4 means one slice out of four equal slices.", "एक वृत्त के दो बराबर भाग करने पर प्रत्येक भाग आधा (1/2) कहलाता है।")
    )


    // --- GENERAL KNOWLEDGE (GK) ---
    val gkSyllabus = listOf(
        CurriculumTopic("Human Body Organs", "मानव शरीर के अंग", "Identify outer sensory organs and internal organs.", "ज्ञानेंद्रियां (आँख, कान, नाक, जीभ, त्वचा) और भीतरी अंग।", "Eyes for sight, Ears for hearing, Nose for smell, Tongue for taste, Skin for touch. Brain controls, Heart pumps.", "आंख देखने के लिए, कान सुनने के लिए। हृदय शरीर में रक्त का संचार करता है।"),
        CurriculumTopic("States of India", "भारत के राज्य", "Quick revision list of major Indian states and active capitals.", "प्रमुख भारतीय राज्य और उनकी राजधानियां।", "New Delhi: Capital of India.\nUttar Pradesh: Lucknow.\nMaharashtra: Mumbai.\nRajasthan: Jaipur.\nBihar: Patna.", "भारत की राजधानी: नई दिल्ली।\nउत्तर प्रदेश: लखनऊ।\nमहाराष्ट्र: मुंबई।\nराजस्थान: जयपुर।"),
        CurriculumTopic("Animal Habitations", "पशुओं के आवास", "Where animals live and their young ones.", "जीव-जंतुओं के रहने का स्थान और उनके बच्चे।", "Dog: Kennel (Puppy).\nLion: Den (Cub).\nHorse: Stable (Foal).\nCow: Shed (Calf).", "शेर: गुफा (शावक)।\nकुत्ता: कैनल (पपी)।\nगाय: शेड (बछड़ा)।")
    )


    // --- REASONING ---
    val reasoningLessons = listOf(
        CurriculumTopic("Odd One Out", "असंगत को ढूँढना", "Identify elements that do not fit in the group.", "वस्तुओं के समूह में जो सबसे अलग हो उसे छांटें।", "Group: [Apple, Banana, Orange, Potato]. 'Potato' is odd because it is a vegetable, others are fruits.", "समूह: [सेब, केला, संतरा, आलू]। आलू सबसे अलग है क्योंकि वह एक सब्जी है, अन्य फल हैं।"),
        CurriculumTopic("Pattern Sequences", "पैटर्न अनुक्रम", "Identify repeating structures and symbols.", "पुनरावर्ती आकृतियों को पूरा करना।", "Sequence: Circle, Triangle, Square, Circle, Triangle, <?>. Correct answer = Square.", "पैटर्न: गोला, त्रिभुज, चौकोर, गोला, त्रिभुज, <?>। सही उत्तर = चौकोर।")
    )


    // --- MORAL STORY COLLECTION (1000+ Stories Meta) ---
    val storiesCollection = listOf(
        CurriculumTopic("The Honest Woodcutter", "ईमानदार लकड़हारा", "An honest woodcutter is rewarded with gold and silver axes by the river goddess.", "एक ईमानदार लकड़हारे को कुल्हाड़ी खो जाने पर सोने-चांदी की कुल्हाड़ी मिलती है।", "The woodcutter dropped his iron axe in the river. He refused the gold and silver axe until the goddess showed his own iron axe. Honesty is the best policy.", "लकड़हारे ने सोने-चांदी की लालच न कर अपनी लोहे की कुल्हाड़ी चुनी। नदी की देवी ने उसकी ईमानदारी से प्रसन्न होकर सब उपहार में दे दीं। सीख: ईमानदारी सबसे अच्छी नीति है।"),
        CurriculumTopic("Unity is Strength", "एकता में बल है", "An old man teaches his fighting sons that a bundle of sticks cannot be broken.", "एक वृद्ध पिता अपने लड़ते हुए बेटों को लकड़ी के गट्ठर की कहानी से एकता समझाता है।", "Single sticks break easily, but bound together they are unbreakable. Together we stand, divided we fall.", "अगर लकड़ियां एक साथ बंधी हों तो कोई तोड़ नहीं सकता। एकता में ही सुरक्षा और शक्ति है।"),
        CurriculumTopic("The Golden Goose", "सोने का अंडा देने वाली मुर्गी", "Greed leads to losing everything.", "लालच में आकर सब कुछ खो देने वाले किसान की कहानी।", "A farmer had a goose that laid a golden egg daily. Desiring all eggs, he cut it open and lost everything. Contentment brings peace.", "एक किसान के पास सोने का अंडा देने वाली मुर्गी थी। उसने सारा सोना एक साथ पाने के लालच में उसे मार दिया और दुखी हुआ। सीख: अति लालच विनाश लाता है।")
    )
}
