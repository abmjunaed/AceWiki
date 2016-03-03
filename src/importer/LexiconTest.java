package importer;

import static org.junit.Assert.*;

import org.junit.Test;

public class LexiconTest {

	public static final String OUT_CSV = "" +
			"ignored\tPrefix(t,http://www.example.org/t.owl#)\n\n" +
			"f\tEvery\n" +
			"cn_sg\thttp://www.example.org/test#man\n" +
			"f\tthat\n" +
			"tv_pl\thttp://www.example.org/test#own\n" +
			"f\ta\n" +
			"cn_sg\thttp://www.example.org/test#bike\n" +
			"f\tand\n" +
			"f\tthat\n" +
			"f\tdoes\n" +
			"f\tnot\n" +
			"tv_sg\thttp://www.example.org/test#own\n" +
			"f\ta\n" +
			"cn_sg\thttp://www.example.org/test#car\n" +
			"f\tis\n" +
			"tv_vbg\thttp://www.example.org/test#like\n" +
			"f\tby\n" +
			"pn_sg\thttp://www.example.org/test#Mary\n" +
			"f\t.\n\n";
	
	public static final String floatingNumber = "" +
			"f\tEvery\r\n" +
			"cn_sg\t/Lavatory_A\r\n" +
			"tv_pl\thttp://www.example.org/test/depth\r\n" +
			"f\t39.0\r\n" +
			"f\t.\r\n"+
			"\r\n";
			//+
//	"f\tEvery\r\n";
//	+
//			
//	"cn_sg\t/ChilledGalley\r\n" +
//	"tv_pl\thttp://www.example.org/test/depth\r\n" +
//	"f\t39.0\r\n" +
//	"f\t.\r\n"+
//	"\r\n";
	@Test
	public void test() {
		ImporterModel m = new ImporterModel();
		m.createLexicon(OUT_CSV);
	}

}