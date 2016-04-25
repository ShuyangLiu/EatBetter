package ur.disorderapp.model;

public class FirebaseData
{
    String phoneNumber;

    String mFood;
    int mAmount;
    String mTime;
    String mLocation;
    String mSituation;
    String mFeeling;

//    public String getFood() {
//        return mFood;
//    }
//
//    public int getAmount() {
//        return mAmount;
//    }
//
//    public String getTimePeriod() {
//        return mTime;
//    }
//
//    public String getLocation() {
//        return mLocation;
//    }
//
//    public String getSituation() {
//        return mSituation;
//    }
//
//    public String getFeeling() {
//        return mFeeling;
//    }

    String mDate;

    public String getDate() {
        return mDate;
    }

    public FirebaseData(SelfAssessmentData body, String phoneNumber) {
        this.phoneNumber = phoneNumber;
        mFood = body.getFood();
        mAmount = body.getAmount();
        mTime = body.getTime().name();
        mLocation = body.getLocation().name();
        mSituation = body.getSituation().name();
        mFeeling = body.getFeeling().name();
        mDate = body.getDate();

    }


    public String getPhoneNumber() {
        return phoneNumber;
    }
}
