import com.bbq.util.task.PdfDailyActiveTask;
import com.bbq.util.task.PybbsShehuiTask;
import com.bbq.util.task.PybbsShuiquInsertTask;


public class APPMain {

	public static void main(String[] args) throws Exception{
		if(args ==null || args.length < 3){
			System.out.println("参数不足");
			System.exit(1);
		}
		String[] param = new String[2];
		param[0] = args[1];
		param[1] = args[2];
		int type = Integer.parseInt(args[0]);
		if(type == 1){
			System.out.println("PybbsShuiquInsertTask");
			PybbsShuiquInsertTask.main(param);
		}else if(type == 2){
			System.out.println("PybbsShehuiTask");
			PybbsShehuiTask.main(param);
		}else if(type==3){
			System.out.println("PdfDailyActiveTask");
			PdfDailyActiveTask.main(param);
		}else{
			System.out.println("未知类型，type="+type);
		}
	}

}
