delete from raw_data.data where source ='4square'



select * from raw_data.data where source ='4square'
select count(*) from raw_data.data where source ='4square'

select * from raw_data.data where data->>'rating'::text = ''
select (data->>'id')::text as jid from raw_data.data group by jid HAVING COUNT(*) > 1

select * from raw_data.data where source ='4square' and data->>'name'::text = 'Meat Shop'

select c.*, bc.count from public.company c, (SELECT count(1) as count from public.branch where company_id in (1) as bc where id in (1,2)

select * from public.branch where name LIKE '%Bob%';
UPDATE public.company SET data = '{ "description": null, "promo": null, "branch_count": 12 }'::json WHERE id = 537;
update public.branch set data = '{ "rubrics": [ { "id": 9 } ], "schedule": { "thursday": [ { "to": "19:00", "from": "09:00" } ], "monday": [ { "to": "19:00", "from": "09:00" } ], "sunday": [], "saturday": [], "friday": [ { "to": "19:00", "from": "09:00" } ], "tuesday": [ { "to": "19:00", "from": "09:00" } ] }, "payment_options": [ { "option": "diners" }, { "option": "mastercard" }, { "option": "visa" }, { "option": "maestro" }, { "option": "visa electron" }, { "option": "mastercard maestro" }, { "option": "visa vale refeição" } ], "address": "Centro, Florianopolis - Santa Catarina, Brazil", "description": "O Rei do Mate é uma casa especializada em chás e cafés. Proporciona aos clientes uma ampla variedade em bebidas como mates, cafés e chocolates quentes e gelados, dentre os gostos conta com cafés expressos, caputinos diversos elaborados com licor e bordas de chocolate. Possui chocolates cremosos e o que dá nome a casa, o mate com acompanhamentos, entre eles, leite, chocolate, açaí, cacau, caju e demais combinações. O destaque fica por conta do mateshake, elaborado com ovomaltine, sorvete de creme e leite em pó. Para completar o lanche o cardápio apresenta opções como salgados, destaque para os sanduíches naturais e quentes, pão de queijo e de batata simples e recheado, croissant, folhados, tortas, além de chocolates e doces.", "raw_schedule": "De Segunda a Sexta, das 09:00 às 19:00", "raw_address": "Rua Vidal Ramos, 110 / Salas 2 e 3 Vidal Ramos Open Shopping - Centro Florianópolis / SC", "contacts": [ { "value": "http://www.reidomate.com.br", "comment": null, "contact": "website" } ], "geometry": { "point": { "lon": -48.5462377, "lat": -27.5965572 } }, "photos": [ "https://irs1.4sqi.net/img/general/width720/35921130_SJl0FrXA2tigqGqQUEn0yIjnPZqvpCCCL1JHsXOhtTc.jpg", "https://irs1.4sqi.net/img/general/width720/niTti00dCbkY7ivLnZJ8A3UwNO8MpQpmXeTHc0FK2UM.jpg", "https://irs1.4sqi.net/img/general/width720/48661845_0ORn-gapUJp5P4r4lQWoi85H-_iOmmb9tBSRPAfdmwA.jpg", "https://irs1.4sqi.net/img/general/width720/52058422__eG5qifxHEe6oAn922VmyDJTqF1o0mZE13q21QQxWYs.jpg" ] }'::json where id = 19;

