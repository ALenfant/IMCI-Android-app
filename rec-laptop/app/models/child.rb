class Child < ActiveRecord::Base
  include Csps::Exportable
  include Csps::Age

  validates_presence_of :first_name, :last_name
  validates_inclusion_of :gender, in: [true, false]

  belongs_to :village, class_name: 'Zone'
  globally_has_many :diagnostics do
    def build_with_answers data={}
      diag = build data
      diag.child = proxy_owner
      diag.prebuild
    end
  end
  globally_has_one :last_visit,
                   class_name: 'Diagnostic', order: 'global_id DESC'
  has_attached_file :photo,
                    path: ':rails_root/public/repo/:global_id_:class_:attachment.:extension',
                    url: '/repo/:global_id_:class_:attachment.:extension',
                    default_url: '/images/missing.png'

  before_save :fill_cache_fields
  
  scope :unfilled, conditions: { first_name: nil, last_name: nil }
  scope :temporary, conditions: { temporary: true }

  VACCINATIONS = {
    bcg_polio0: 'BCG/Polio-0',
    penta1_polio1: 'PENTA-1/Polio-1',
    penta2_polio2: 'PENTA-2/Polio-2',
    penta3_polio3: 'PENTA-3/Polio-3',
    measles: 'Antirougeoleux' }

  def vaccinations
    VACCINATIONS.select { |k,v| send(k) }.map &:last
  end

  def name
    "#{first_name} #{last_name}"
  end

  delegate :index, :index_ratio, to: :last_visit, allow_nil: true
  for name, ratio in Diagnostic::INDICES do
    delegate name, ratio, to: :last_visit, allow_nil: true
  end
  
  def self.group_stats_by status, rs, conds
    m = self.minimum(:created_at)
    return {} if m.nil?
    d1 = m.beginning_of_month
    d2 = d1.next_month.to_date
    grs = {}
    signs = {}
    while Date.today.next_month.beginning_of_month >= d2
      diagnosed = Diagnostic.between(d1, d2).includes(results: :classification)#.all
      conds.each do |cond|
        case cond['field']
        when 'classifications' then
          diagnosed = diagnosed.select do |d|
            d.results.map { |r| r.classification.name }.send(cond['operator'], cond['value'])
          end
        when /sign_answers\[\w+\]/ then
          # get wanted sign (with caching)
          sign_key = cond['field'].scan(/sign_answers\[(\w+)\]/).first.first
          signs[sign_key] ||= Sign.find_by_key(sign_key)
          # get answer
          diagnosed = diagnosed.select do |d|
            answer = d.sign_answers.find_by_sign_id(signs[sign_key].id)
            if answer
              answer.raw_value.send(cond['operator'], answer.class.cast(cond['value']))
            else
              false
            end
          end
        end
      end
      diagnosed = diagnosed.map &:child_global_id
      k = dates2key(d1)
      grs[k] = 0
      rs.each do |r|
        case status
        when 'new' then
          grs[k] += 1 if r.created_at >= d1 and r.created_at < d2
        when 'old' then
          grs[k] += 1 if r.created_at < d1 and diagnosed.include? r.global_id
        when 'follow' then
          grs[k] += 1 if diagnosed.include? r.global_id
        end
      end
      d1 = d1.next_month
      d2 = d2.next_month
    end
    grs
  end
  
  def self.dates2key d
    "#{d.year}-#{sprintf("%02d", d.month)}"
  end

  protected

  @@iconv = Iconv.new('ASCII//IGNORE//TRANSLIT', 'UTF-8')
  def fill_cache_fields
    self.cache_name = name.cacheize
  end
end
