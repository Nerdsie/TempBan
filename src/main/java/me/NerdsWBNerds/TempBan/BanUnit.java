package me.NerdsWBNerds.TempBan;

public enum BanUnit {
	SECOND("s", 1.0/60.0), HOUR("h", 60), DAY("d", 60*24), WEEK("w", 60*24*7), MONTH("mo", 30*60*24), MINUTE("m", 1), YEAR("y", 30*60*24*12);
	
	public String name;
	public double multi;
	
	BanUnit(String n, double mult){
		name = n;
		multi = mult;
	}
	
	public static long getTicks(String un, int time){
		long sec;
		
		try{
			sec = time * 60;
		}catch(NumberFormatException ex){
			return 0;
		}
		
		for(BanUnit unit: BanUnit.values()){
			if(un.startsWith(unit.name)){
				return (long) (sec * unit.multi *1000.0);
			}
		}
		
		return 0;
	}
}
