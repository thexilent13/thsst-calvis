execute(src, registers, memory) {
	Calculator c = new Calculator(registers, memory);
    if ( src.isMemory() ) {
		
        int size = memory.getBitSize(src);
        String value = memory.read(src, size);
		double spValue = 0.0;
		spValue = Integer.parseInt(value, 16) + 0.0;
		
        String st0 = registers.get("ST0");
        double stValue = Double.parseDouble(st0);
		double resultingValue =  spValue / stValue;
		if(resultingValue > Math.pow(2,64)){
			registers.mxscr.setOverflowFlag("1");
		}
		else if(resultingValue < Math.pow(2, 64) * -1){
			registers.mxscr.setUnderflowFlag("1");
		}
		else{
			//System.out.println(resultingValue + " value");
			registers.set("ST0", "" + resultingValue);
		}
        
		registers.x87().status().set("C3",'0');
		registers.x87().status().set("C2",'0');
		registers.x87().status().set("C0",'0');
    }
}
