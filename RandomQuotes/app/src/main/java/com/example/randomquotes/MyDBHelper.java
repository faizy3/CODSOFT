package com.example.randomquotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "quotesdb";
    private static final String TABLE_QUOTES = "quotes";
    private static final String TABLE_FAVQUOTES = "favoritequotes";
    private static final String COLUMN_QUOTE_ID = "quote_id";
    private static final String COLUMN_FAVORITEQUOTE_ID = "favoritequote_id";
    private static final String COLUMN_QUOTE = "quote";
    private static final String COLUMN_FAVORITE_QUOTE = "favorite_quote";
    Context context;
    public MyDBHelper(Context context) {
        super(context , DATABASE_NAME , null , DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //for creating quotes table
        String Quote = "CREATE TABLE " + TABLE_QUOTES +  " ( "
                + COLUMN_QUOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_QUOTE + " VARCHAR "
                + ")";
        db.execSQL(Quote);
        //for creating favorite quotes table
        String favoriteQuote = "CREATE TABLE " + TABLE_FAVQUOTES +  " ( "
                + COLUMN_FAVORITEQUOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FAVORITE_QUOTE + " VARCHAR "
                + ")";
        db.execSQL(favoriteQuote );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_QUOTES);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_FAVQUOTES);
        onCreate(db);

    }
    public void insertFavoriteQuote(QuotesModel quotesModel){
        SQLiteDatabase db  = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FAVORITE_QUOTE , quotesModel.getFavoritequote());
        db.insert(TABLE_FAVQUOTES , null , contentValues);
        db.close();
    }
    public void insertQuote(){
        SQLiteDatabase db  = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String [] quotes = {"“When you forgive you win.”",
                "“Forgiveness is the power to choose how things affect you.”",
                "“Forgiveness is choosing to be happy.”",
                "“Fear is wisdom as a child.”",
                "“Forgiveness is always possible, but reconciliation is not always possible.”",
                "“To be able to forgive we must first meet the legitimate needs of the s of us that don’t want to forgive.”",
                "“A more forgiving attitude makes it easier to develop wisdom…Likewise a deeper capacity for wisdom makes it easier to forgive.”",
                "“Practising forgiveness on the small things makes it easier to forgive the big things.”",
                "“A garden emerges from tending the outer world; forgiveness emerges from tending our inner world.”",
                "“What we think are issues we have with our nts are really issues we have with life.”",
                "“Self forgiveness is one of the most unselfish things you can do. Everyone around benefits as you become less demanding, more giving and more forgiving. – Four Steps to Forgiveness, William Fergus Martin”",
                "“I am not afraid of storms, for I am learning how to sail my ship.”",
                "“Tomorrow is always fresh, with no mistakes in yet.”",
                "“It’s the possibility of having a dream come true that makes life interesting.”",
                "“Most of us can’t help but live as though we’ve got two lives to live, one is the mockup, the other the finished version, and then there are all those versions in between.”",
                "“All we have to decide is what to do with the time that is given to us.”",
                "“In spite of everything I still believe that people are really good at heart.”",
                "“What’s the point of having a voice if you’re gonna be silent in those moments you shouldn’t be?”",
                "“Just think happy thoughts and you’ll fly.”",
                "“It’s not destroying. It’s making something new.” ",
                "“When will the lesson be learned! You cannot reason with a tiger when your head is in its mouth!” ",
                "“Do, or do not. There is no “try”.",
                "“Life is not the amount of breaths you take. It’s the moments that take your breath away.”",
                "“It’s only after we’ve lost everything that we’re free to do anything.”",
                "“Oh yes, the past can hurt. But you can either run from it, or learn from it.”",
                "“People often say that motivation doesn't last. Well, neither does bathing -- that's why we recommend it daily.“",
                "“If your voice is high, only a few people will hear. If your thought is high, then many people will listen.”",
                "“The bad thing is that the time is short… and the good thing is that there is still some time..”",
                "“The journey towards success begins with a most important step – just begin”",
                "“It is during our darkest moments that we must focus to see the light.”",
                "“If you’re the smartest person in the room, for sure you’re in the wrong room.”",
                "“The best things are learnt during the worst days of your life. Get up and see what new learning you had on a bad day.”",
                "“Always make a total effort, even when the odds are against you.”",
                "“Belief and hope don’t make goals easy… but make them possible.”",
                "“The best way to get started is to quit talking and begin doing.”",
                "“The best way to predict the future is to create it.”",
                "“The pessimist sees difficulty in every opportunity. The optimist sees opportunity in every difficulty.”",
                "“We are what we repeatedly do. Excellence, then, is not an act, but a habit.”",
                "“Life is not about waiting for the storm to pass; it is about learning to dance in the rain.”",
                "“Great minds discuss ideas; average minds discuss events; small minds discuss people.”",
                "“You can never cross the ocean until you have the courage to lose sight of the shore.”",
                "“I have not failed. I’ve just found 10,000 ways that won’t work.”",
                "“If you work on something a little bit every day, you end up with something that is massive.”",
                "“Nothing will work unless you do.”",
                "“One way to keep momentum going is to have constantly greater goals.”",
                "“What lies behind us and what lies before us are tiny matters compared to what lies within us.”",
                "“Rainbows are formed only when both rain and sunshine meet. Happiness and sorrow both are necessary for a beautiful life.”",
                "“Life is difficult because… we don’t appreciate the things that come easily.”",
                "“Where expectations end… peace begins there”",
                "“Waking up early is always beneficial… be it from sleep or ego or delusion.”",
                "“Make your existence such that… even if someone leaves you, he cannot forget you.”",
                "“Start your day with doing good things for other, your day will become bound to be good.”",
                "“The origin of estrangement and river is very small… but as it progresses, it takes a huge form.”",
                "“A man’s best friend is his health… If health leaves you, then you become a burden for every relationship.” ",
                "“The more tolerance, forgiveness and understanding in a relationship…. the longer the relationship will be.”",
                "“If you want to bloom like a rose…then you have to compromise with thorns”",
                "“I can’t change the direction of the wind, but I can adjust my sails to always reach my destination.”",
                "“It is never too late to be what you might have been.”",
                "“The number one reason people fail in life is because they listen to their friends, family, and neighbors.”",
                "“The whole secret of a successful life is to find out what is one’s destiny to do, and then do it.”",
                "“You may have to fight a battle more than once to win it.”",
                "“A successful man is one who can lay a firm foundation with the bricks others have thrown at him.”",
                "“No one can make you feel inferior without your consent.”  ",
                "“Don’t be afraid to give up the good to go for the great.”",
                "“Change your thoughts and you change your world.”",
                "“Find a victory in every defeat to remain hopeful, and find a defeat in every victory to remain humble.”",
                "“Strength does not come from winning. Your struggles develop your strengths. When you go through hardships and decide not to surrender, that is strength.”",
                "“Only put off until tomorrow what you are willing to die having left undone.”",
                "“Live as if you were to die tomorrow. Learn as if you were to live forever.”",
                "“If it doesn’t challenge you, it doesn’t change you.”",
                "“Thinking should become your capital asset, no matter whatever ups and downs you come across in your life.”",
                "“The capacity to learn is a gift; the ability to learn is a skill; the willingness to learn is a choice.”",
                "“What seems to us as bitter trials are often blessings in disguise.”",
                " “There are two types of people who will tell you that you cannot make a difference in this world: those who are afraid to try and those who are afraid you will succeed.”",
                "“Our greatest weakness lies in giving up. The most certain way to succeed is always to try just one more time.”",
                "“The greatest weapon against stress is the ability to choose one thought over another.” ",
                "“My attitude is that if you push me towards something that you think is a weakness, then I will turn that perceived weakness into a strength.”",
                "“Dream as if you’ll live forever, live as if you’ll die today.”",
                "“A person who never made a mistake never tried anything new.”",
                "“When one door of happiness closes, another opens; but often we look so long at the closed door that we do not see the one which has been opened for us.”",
                "“Focus on the journey, not the destination. Joy is found not in finishing an activity but in doing it.”",
                "“If you start to think the problem is ‘out there,’ stop yourself. That thought is the problem.”",
                "“The best way out is always through.” ",
                "“Don’t judge each day by the harvest you reap but by the seeds that you plant.”",
                "“Do what you have to do until you can do what you want to do.”",
                "“If you’re going through hell, keep going”",
                "“Losers quit when they fail. Winners fail until they succeed.”",
                "“A winner is a dreamer who never gives up.”",
                "“A champion is afraid of losing. Everyone else is afraid of winning.”",
                "“You were born to win, but to be a winner, you must plan to win, prepare to win, and expect to win.”",
                "“One thing’s for sure, if you don’t play, you don’t win.”",
                "“It’s okay to be a glow stick : Sometimes we have to break before we shine.”",
                "“The man who does not read has no advantage over the man who cannot read.”",
                "“If you talk about it, it’s a dream, if you envision it, it’s possible, but if you schedule it, it’s real.”",
                "“Life moves pretty fast. If you don’t stop and look around once in a while, you could miss it.”",
                "“In order to succeed, your desire for success should be greater than your fear of failure.”",
                "“The best revenge is massive success.”",
                "“I am thankful for all of those who said NO to me. Its because of them I’m doing it myself.” ",
                "“Optimism is a happiness magnet. If you stay positive good things and good people will be drawn to you.”",
                "“Nobody ever wrote down a plan to be broke, fat, lazy, or stupid. Those things are what happen when you don’t have a plan.”",
                "“Though no one can go back and make a brand new start, anyone can start from now and make a brand new ending.”",
                "“The way to get started is to quit talking and begin doing.”",
                "“Most people give up just when they’re about to achieve success. They quit on the one yard line. They give up at the last-minute of the game, one foot from a winning touchdown.”",
                "“Don’t let yesterday take up too much of today.”",
                "“People who are crazy enough to think they can change the world, are the ones who do.” ",
                "“Only those who will risk going too far can possibly find out how far one can go.”",
                "“Never confuse a single defeat with a final defeat.”",
                "“The secret of getting ahead is getting started.”",
                "“Although the world is full of suffering, it is also full of the overcoming of it.”",
                "“Shoot for the moon and if you miss you will still be among the stars.”",
                "“Not all those who wander are lost.”",
                "“In order to attain the impossible, one must attempt the absurd.”",
                "“Motivation is what gets you started. Habit is what keeps you going.”",
                "“To accomplish great things, we must not only act, but also dream, not only plan, but also believe.”",
                "“Most of the important things in the world have been accomplished by people who have kept on trying when there seemed to be no help at all.”",
                "“If you cannot do great things, do small things in a great way.” ",
                "“Winning doesn’t always mean being first. Winning means you’re doing better than you’ve done before.”",
                "“Life is 10% what happens to you and 90% how you react to it.”",
                "“With the new day comes new strength and new thoughts.”",
                "“It always seems impossible until it’s done.”",
                "“Real difficulties can be overcome; it is only the imaginary ones that are unconquerable.”",
                "“Perseverance is a great element of success; if you only knock long enough and loud enough at the gate you are sure to wake up somebody.”",
                "“Great things never came from comfort zones.”",
                "“Set your goals high, and don’t stop till you get there.”",
                "“You can’t cross the sea merely by standing and staring at the water.”",
                "“No one is perfect – that’s why pencils have erasers.”",
                "“Keep your face to the sunshine and you cannot see a shadow.”",
                "“People often say that motivation doesn’t last. Well, neither does bathing — that’s why we recommend it daily.”",
                "“Someday is not a day of the week.”",
                "“Your time is limited, so don’t waste it living someone else’s life.”",
                "“If you are not taking care of your customer, your competitor will.”",
                "“Problems are not stop signs, they are guidelines.”",
                "“If you fell down yesterday, stand up today.”",
                "“You’re off to great places, today is your day. Your mountain is waiting, so get on your way.”",
                "“You always pass failure on the way to success.”",
                "“Beware of monotony; it’s the mother of all the deadly sins.”",
                "“I’d rather regret the things I’ve done than regret the things I haven’t done.”",
                "“Always do your best. What you plant now, you will harvest later.”",
                "“Move out of your comfort zone. You can only grow if you are willing to feel awkward and uncomfortable when you try something new.”",
                "“Don’t let the fear of losing be greater than the excitement of winning.”",
                "“Energy and persistence conquer all things.”",
                "“Obstacles are those frightful things you see when you take your eyes off your goal.”",
                "“You can waste your lives drawing lines. Or you can live your life crossing them.”",
                "“Done is better than perfect.”",
                "“Success is not final, failure is not fatal: it is the courage to continue that counts.”"
        };
//        contentValues.put(COLUMN_QUOTE , "“When you forgive you win.”");
//        contentValues.put(COLUMN_QUOTE , "“Forgiveness is the power to choose how things affect you.”");
//        contentValues.put(COLUMN_QUOTE , "“Forgiveness is choosing to be happy.”");
        for(int i=0; i<quotes.length; i++){
            String quotevalue = quotes[i];
            contentValues.put(COLUMN_QUOTE , quotevalue);
            db.insert(TABLE_QUOTES , null , contentValues);
        }

        db.close();
        Toast.makeText(context, "Date Inserted", Toast.LENGTH_SHORT).show();
    }
    public void deleteQuoteFromFavQuotes(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FAVQUOTES , COLUMN_FAVORITEQUOTE_ID + "=?" , new String[]{String.valueOf(id)});
    }
}
