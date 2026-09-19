import React,{useState} from 'react';
import {ArrowLeft,BookOpen,CheckCircle2,Compass,Flame,GraduationCap,Heart,MapPin,RefreshCw,ScrollText,Star,Target} from 'lucide-react';

type ModuleId='QIBLA'|'RAMADAN'|'HAJJ'|'SEERAH'|'QUIZ';
const modules=[
 {id:'QIBLA' as const,title:'কিবলা',icon:Compass,desc:'ব্রাউজারের GPS ও orientation ব্যবহার করে কাবার দিক নির্ণয় করুন।'},
 {id:'RAMADAN' as const,title:'রমজান',icon:Flame,desc:'রোজা, সাহরি-ইফতার, আমল ও রমজানের গুরুত্বপূর্ণ তথ্য।'},
 {id:'HAJJ' as const,title:'হজ',icon:MapPin,desc:'ইহরাম থেকে তাওয়াফ ও সাঈ—ধাপে ধাপে হজ গাইড।'},
 {id:'SEERAH' as const,title:'সীরাত',icon:ScrollText,desc:'রাসূল ﷺ-এর জীবনের গুরুত্বপূর্ণ ঘটনাগুলোর ধারাবাহিক পাঠ।'},
 {id:'QUIZ' as const,title:'কুইজ',icon:GraduationCap,desc:'ইসলামিক জ্ঞান যাচাই ও শেখার জন্য ছোট কুইজ।'}
];

const quiz=[
 ['কুরআনে মোট কতটি সূরা?',['১১০','১১৪','১২০'],'১১৪'],
 ['ইসলামের পাঁচটি স্তম্ভের প্রথমটি কোনটি?',['সালাত','সাওম','শাহাদাহ'],'শাহাদাহ'],
 ['রমজানে ফরজ ইবাদত কোনটি?',['সাওম','হজ','কুরবানি'],'সাওম'],
 ['কাবা শরিফ কোথায় অবস্থিত?',['মদিনা','মক্কা','জেরুজালেম'],'মক্কা'],
 ['ফজরের ফরজ নামাজ কত রাকাত?',['২','৩','৪'],'২']
] as const;

export const WebModulesScreen:React.FC<{onBack:()=>void}>=({onBack})=>{
 const [active,setActive]=useState<ModuleId|null>(null);
 const [q,setQ]=useState(0); const [score,setScore]=useState(0); const [answered,setAnswered]=useState(false);
 const [heading,setHeading]=useState(0); const [geo,setGeo]=useState<string>('অবস্থান নেওয়া হয়নি'); const [qibla,setQibla]=useState<number|null>(null); const [compassActive,setCompassActive]=useState(false);
 const select=(id:ModuleId)=>{setActive(id);setQ(0);setScore(0);setAnswered(false);};
 const locate=()=>navigator.geolocation?.getCurrentPosition(p=>{const lat=p.coords.latitude,lon=p.coords.longitude;setGeo(lat.toFixed(5)+', '+lon.toFixed(5));const kaabaLat=21.422487*Math.PI/180,kaabaLon=39.826206*Math.PI/180,phi=lat*Math.PI/180,lam=lon*Math.PI/180;const y=Math.sin(kaabaLon-lam)*Math.cos(kaabaLat);const x=Math.cos(phi)*Math.sin(kaabaLat)-Math.sin(phi)*Math.cos(kaabaLat)*Math.cos(kaabaLon-lam);setQibla((Math.atan2(y,x)*180/Math.PI+360)%360)},()=>setGeo('অবস্থান অনুমতি পাওয়া যায়নি')); const startCompass=async()=>{try{const D=DeviceOrientationEvent as unknown as {requestPermission?:()=>Promise<string>};if(D.requestPermission){const ok=await D.requestPermission();if(ok!=='granted')return;}window.addEventListener('deviceorientationabsolute',onOrientation as EventListener);window.addEventListener('deviceorientation',onOrientation as EventListener);setCompassActive(true)}catch{setCompassActive(false)}}; const onOrientation=(e:DeviceOrientationEvent)=>{const a=e.alpha;if(a!=null)setHeading(a)};
 const answer=(a:string)=>{if(answered)return;setAnswered(true);if(a===quiz[q][2])setScore(s=>s+1);};
 return <div className="max-w-4xl mx-auto px-4 py-6 space-y-5">
  <button onClick={onBack} className="flex items-center gap-2 text-sm font-bold text-[#176B4D]"><ArrowLeft className="w-4 h-4"/>ফিরে যান</button>
  {!active?<><div className="rounded-3xl p-6 bg-gradient-to-r from-[#176B4D] to-[#0A3D2B] text-white"><h1 className="text-2xl font-black">ইসলামিক জ্ঞান ও আমল</h1><p className="mt-2 text-sm text-white/80">ওয়েবেই সবকিছু—কোনো Android-specific অংশের ওপর নির্ভরশীল নয়।</p></div><div className="grid sm:grid-cols-2 gap-4">{modules.map(m=>{const I=m.icon;return <button key={m.id} onClick={()=>select(m.id)} className="text-left p-5 rounded-3xl bg-white dark:bg-[#1A221C] border border-[#E8EFEA] dark:border-[#3A4D43]/60"><I className="w-7 h-7 text-[#176B4D]"/><h2 className="mt-3 font-black">{m.title}</h2><p className="mt-1 text-xs text-[#717A74]">{m.desc}</p></button>})}</div></>:
  <div className="rounded-3xl p-5 bg-white dark:bg-[#1A221C] border border-[#E8EFEA] dark:border-[#3A4D43]/60">
   <button onClick={()=>setActive(null)} className="text-xs font-bold text-[#176B4D]">← সব মডিউল</button>
   {active==='QIBLA'&&<div className="text-center py-8 space-y-5"><Compass className="w-24 h-24 mx-auto text-[#176B4D]" style={{transform:'rotate('+((qibla??0)-heading)+'deg)'}}/><h2 className="text-xl font-black">কিবলা কম্পাস</h2><p className="text-xs text-[#717A74]">কাবার দিক পেতে GPS ও device orientation অনুমতি দিন।</p><button onClick={locate} className="px-4 py-2 rounded-xl bg-[#176B4D] text-white text-xs font-bold">GPS অবস্থান</button><p className="text-xs">{geo}</p><button onClick={startCompass} className="px-4 py-2 rounded-xl border text-xs font-bold">{compassActive?'কম্পাস চালু':'কম্পাস চালু করুন'}</button>{qibla!=null&&<p className="text-xs font-bold">কিবলা বিয়ারিং: {qibla.toFixed(1)}°</p>}</div>}
   {active==='RAMADAN'&&<Guide title="রমজান" items={['সাহরি ও ইফতার সময় স্থানীয় নির্ভরযোগ্য সময়সূচি অনুযায়ী অনুসরণ করুন।','রোজার মূল উদ্দেশ্য তাকওয়া অর্জন ও আত্মসংযম।','কুরআন তিলাওয়াত, সালাত, দান-সদকা ও দোয়ার প্রতি গুরুত্ব দিন।','অসুস্থতা বা শরয়ি বিশেষ অবস্থায় রোজার বিধান আলেমের কাছে জেনে নিন।']}/>}
   {active==='HAJJ'&&<Guide title="হজের ধাপ" items={['ইহরাম ও নিয়ত','মক্কায় পৌঁছে তাওয়াফ','সাফা-মারওয়ার মাঝে সাঈ','মিনা, আরাফাত ও মুযদালিফার আমল','জামারাতে রমি','তাওয়াফে ইফাদা ও প্রয়োজনীয় সমাপ্তির আমল']}/>}
   {active==='SEERAH'&&<Guide title="সীরাত পাঠ" items={['মক্কায় জন্ম ও শৈশব','ওহি নাজিল ও নবুওয়াতের সূচনা','মক্কী দাওয়াত ও নির্যাতন','হিজরত ও মদিনার সমাজ','বদর, উহুদ ও গুরুত্বপূর্ণ ঘটনাবলি','বিদায় হজ ও শেষ জীবনের শিক্ষা']}/>}
   {active==='QUIZ'&&<div className="py-5 space-y-5"><div className="flex justify-between text-xs font-bold"><span>প্রশ্ন {q+1}/{quiz.length}</span><span>স্কোর: {score}</span></div><h2 className="text-xl font-black">{quiz[q][0]}</h2><div className="grid gap-2">{quiz[q][1].map(a=><button key={a} onClick={()=>answer(a)} className="text-left p-4 rounded-2xl border font-semibold">{a}</button>)}</div>{answered&&(q<quiz.length-1?<button onClick={()=>{setQ(x=>x+1);setAnswered(false)}} className="px-4 py-2 rounded-xl bg-[#176B4D] text-white text-xs font-bold">পরের প্রশ্ন</button>:<div className="p-4 rounded-2xl bg-[#E8EFEA] font-bold">কুইজ শেষ — স্কোর {score}/{quiz.length} <button className="ml-3 underline" onClick={()=>{setQ(0);setScore(0);setAnswered(false)}}>আবার শুরু</button></div>)}</div>}
  </div>}
 </div>;
};
const Guide:React.FC<{title:string,items:string[]}>=({title,items})=><div className="py-5 space-y-4"><h2 className="text-2xl font-black">{title}</h2>{items.map((x,i)=><div key={x} className="flex gap-3 p-4 rounded-2xl bg-[#F4F8F5] dark:bg-[#1E2821]"><CheckCircle2 className="w-5 h-5 text-[#176B4D] shrink-0"/><div><b>{i+1}. </b>{x}</div></div>)}</div>;
